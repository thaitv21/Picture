package com.nullexcom.picture

import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.core.net.toFile
import com.nullexcom.picture.data.*
import com.nullexcom.picture.imageprocessor.ImageExporter
import com.nullexcom.picture.ui.EditorState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.*
import java.util.*

class EditorViewModel(intent: Intent) {
    val appState = AppState
    val isLoading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    private val pageState = BehaviorSubject.createDefault<EditorState>(EditorState.Filter.apply { shouldCreate = true })

    lateinit var originalBitmap: Bitmap
    private var insertedUri: Uri? = null

    private var name: String = intent.getStringExtra("name") ?: ""
    private var uri: Uri = intent.getParcelableExtra("uri") ?: Uri.EMPTY
    private lateinit var photo: Photo
    private lateinit var template: Template
    private val stack = Stack<Bitmap>()

    init {
        if (Uri.EMPTY == uri) {
            pageState.onNext(EditorState.Error)
        } else {
            photo = Photo.fromUri(uri)
            template = photo.template
            originalBitmap = decodeImage(uri).copy(Bitmap.Config.ARGB_8888, true)
            stack.push(originalBitmap)
        }
    }

    fun pushBitmap(bitmap: Bitmap) {
        stack.push(bitmap)
    }

    fun getCurrentBitmap(): Bitmap {
        return stack.peek()
    }

    fun popBitmap() {
        stack.pop()
    }

    fun getPhoto(): Photo {
        return photo
    }

    fun getTemplate(): Template {
        return template
    }

    fun getPageState(): Observable<EditorState> {
        return pageState
    }

    fun onNextPage() {
        val nextPage = when (pageState.value) {
            EditorState.Adjust -> EditorState.Filter
            EditorState.Filter -> EditorState.Histogram
            EditorState.Histogram -> EditorState.Blur
            EditorState.Blur -> EditorState.AskSave.apply { previousPage = EditorState.Blur }
            else -> null
        }
        nextPage?.let { pageState.onNext(it.apply { shouldCreate = true }) }
    }


    fun onBack() {
        val previousPage = when (pageState.value) {
            EditorState.Filter -> EditorState.ShouldFinish
            EditorState.Histogram -> EditorState.Filter
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
        CoroutineScope(Dispatchers.IO).launch {
            val imageExporter = ImageExporter(photo, filename, template)
            insertedUri = imageExporter.export()
            withContext(Dispatchers.Main) {
                pageState.onNext(EditorState.Completed.apply { shouldCreate = true })
            }
        }
    }

    fun setWallpaper(context: Context) {
        val bitmap = stack.peek()
        val manager = WallpaperManager.getInstance(context)

        Completable.defer {
            manager.setBitmap(bitmap)
            Completable.complete()
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
            isLoading.onNext(true)
        }.doOnComplete {
            pageState.onNext(EditorState.Done)
        }.subscribe()


    }

    fun share() {
        if (insertedUri == null) return
        isLoading.onNext(true)
        val share = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, insertedUri)
        }
        isLoading.onNext(false)
        finish()
        val applicationContextCompat = ApplicationContextCompat.getInstance()
        val context = applicationContextCompat.getApplicationContext()
        val intent = Intent.createChooser(share, "Share your picture to").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    fun publishImage() {
        val uri = insertedUri ?: return
        isLoading.onNext(true)
        CoroutineScope(Dispatchers.IO).async {
            val uploadedUri = Firebase.uploadImage(name, uri)
            Firebase.addImage(uploadedUri, template)
        }.invokeOnCompletion {
            isLoading.onNext(false)
            pageState.onNext(EditorState.Done)
        }
    }

    fun finish() {
        pageState.onNext(EditorState.Done)
    }

    fun completeEditing() {
        pageState.onNext(EditorState.AskSave.apply {
            shouldCreate = true
            previousPage = pageState.value
        })
    }
}