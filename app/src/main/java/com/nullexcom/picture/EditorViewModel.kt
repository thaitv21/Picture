package com.nullexcom.picture

import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.net.toFile
import com.nullexcom.picture.data.*
import com.nullexcom.picture.ui.EditorState
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.*

class EditorViewModel(intent: Intent) {
    val appState = AppState

    private val pageState = BehaviorSubject.createDefault<EditorState>(EditorState.Histogram.apply { shouldCreate = true })

    lateinit var originalBitmap: Bitmap
    private var currentBitmap: Bitmap? = null
    private var insertedUri: Uri? = null
    val template = Template()

    private var name: String = intent.getStringExtra("name") ?: ""
    private var uri: Uri = intent.getParcelableExtra("uri") ?: Uri.EMPTY

    init {
        if (Uri.EMPTY == uri) {
            pageState.onNext(EditorState.Error)
        } else {
            originalBitmap = decodeImage(uri).copy(Bitmap.Config.ARGB_8888, true)
        }
    }

    fun getPageState(): Observable<EditorState> {
        return pageState
    }

    fun getCropped(): Bitmap {
        return originalBitmap
    }

    fun getFilteredBitmap(): Bitmap {
        return currentBitmap ?: getCropped()
    }

    fun setFilteredBitmap(bitmap: Bitmap) {
        currentBitmap = bitmap
    }

    fun onNextPage() {
        val nextPage = when (pageState.value) {
            EditorState.Adjust -> EditorState.Histogram
            EditorState.Histogram -> EditorState.Blur
            EditorState.Blur -> EditorState.AskSave.apply { previousPage = EditorState.Blur }
            else -> null
        }
        nextPage?.let { pageState.onNext(it.apply { shouldCreate = true }) }
    }


    fun onBack() {
        val previousPage = when (pageState.value) {
            EditorState.Histogram -> EditorState.ShouldFinish
            EditorState.Blur -> EditorState.Histogram
            EditorState.More -> EditorState.Blur
            else -> null
        }
        previousPage?.let { pageState.onNext(it.apply { shouldCreate = false }) }
    }

    fun onCancelSaving() {
        val currentPage = pageState.value
        if (currentPage is EditorState.AskSave) {
            val previousPage = currentPage.previousPage ?: return
            pageState.onNext(previousPage.apply { shouldCreate = false })
        }
    }

    private fun decodeImage(uri: Uri): Bitmap {
        val file = uri.toFile()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(file)
            return ImageDecoder.decodeBitmap(source)
        }
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        return BitmapFactory.decodeFile(file.absolutePath, options)
    }

    fun saveImage(filename: String) {
        pageState.onNext(EditorState.Saving.apply { shouldCreate = true })
        val bitmap = currentBitmap ?: return
        CoroutineScope(Dispatchers.IO).launch {
            val imageExporter = ImageExporter(bitmap, uri, filename, template)
            insertedUri = imageExporter.export()
            withContext(Dispatchers.Main) {
                pageState.onNext(EditorState.Completed.apply { shouldCreate = true })
            }
        }
    }

    fun setWallpaper(context: Context) {
        val bitmap = currentBitmap ?: return
        val manager = WallpaperManager.getInstance(context)
        manager.setBitmap(bitmap)
    }

    fun share(context: Context) {
        if (insertedUri == null) return
        val share = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, insertedUri)
        }
        context.startActivity(Intent.createChooser(share, "Share your picture to"))
    }

    fun publishImage() {
        val uri = insertedUri ?: return
        CoroutineScope(Dispatchers.IO).async {
            val uploadedUri = Firebase.uploadImage(name, uri)
            Firebase.addImage(uploadedUri, template)
        }.invokeOnCompletion {
            pageState.onNext(EditorState.Done)
        }
    }
}