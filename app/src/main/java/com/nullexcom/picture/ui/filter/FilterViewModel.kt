package com.nullexcom.picture.ui.filter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Size
import androidx.core.net.toFile
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.nullexcom.picture.ApplicationContextCompat
import com.nullexcom.picture.EditorViewModel
import com.nullexcom.picture.imageprocessor.ImageProcessor
import com.nullexcom.picture.data.Photo
import com.nullexcom.picture.data.SampleTemplate
import com.nullexcom.picture.data.Template
import com.nullexcom.picture.data.repo.TemplateRepository
import com.nullexcom.picture.viewmodels.AppViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.SingleSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class FilterViewModel(private val photo: Photo) : AppViewModel(), LifecycleObserver {
    private val uri = photo.uri
    private lateinit var srcBitmap: Bitmap
    private val bitmap = BehaviorSubject.create<Bitmap>()
    private val templates: SingleSubject<List<Template>> = SingleSubject.create<List<Template>>()
    private var currentTemplate: Template? = null
    private val applicationContextCompat: ApplicationContextCompat by lazy { ApplicationContextCompat.getInstance() }
    private val templateRepository: TemplateRepository by lazy { TemplateRepository() }
    private val imageProcessor: ImageProcessor by lazy {
        ImageProcessor(applicationContextCompat.getApplicationContext(), photo, applicationContextCompat.preferBitmapSize())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        GlobalScope.launch {
            srcBitmap = decodeImage(uri)
            val out = srcBitmap.copy(Bitmap.Config.ARGB_8888, true)
            applyFilter(out, photo.template, srcBitmap)
            withContext(Dispatchers.Main) {
                bitmap.onNext(out)
                templates.onSuccess(templateRepository.getTemplates())
            }
        }
    }

    fun getBitmap(): Observable<Bitmap> {
        return bitmap
    }

    fun getTemplates(): Single<List<Template>> {
        return templates
    }

    fun onTemplate(template: Template) {
        val bmp = bitmap.value
        applyFilter(bmp, template, srcBitmap)
    }

    private fun applyFilter(bmp: Bitmap, template: Template, srcBitmap: Bitmap?) {
        currentTemplate = template
        GlobalScope.launch {
            if (srcBitmap == null) {
                imageProcessor.blend(bmp, template)
            } else {
                imageProcessor.blend(srcBitmap, bmp, template)
            }
            withContext(Dispatchers.Main) {
                bitmap.onNext(bmp)
            }
        }
    }

    private fun decodeImage(uri: Uri): Bitmap {
        val file = uri.toFile()
        val size = calculateImageSize(file)
        val reqSize = calculateRequestSize()
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inSampleSize = calculateInSampleSize(size, reqSize)
        return BitmapFactory.decodeFile(file.absolutePath, options)
    }

    private fun calculateImageSize(file: File): Size {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(file.absolutePath, options)
        val imageHeight: Int = options.outHeight
        val imageWidth: Int = options.outWidth
        return Size(imageWidth, imageHeight)
    }

    private fun calculateInSampleSize(size: Size, requestSize: Size): Int {
        val (height: Int, width: Int) = size.run { height to width }
        val (reqHeight: Int, reqWidth) = requestSize.run { requestSize.height to requestSize.width }
        if (reqHeight >= height && reqWidth >= width) {
            return 1
        }
        var inSampleSize = 2
        while (width / inSampleSize > reqWidth && height / inSampleSize > height) {
            inSampleSize *= 2
        }
        return inSampleSize
    }

    private fun calculateRequestSize(): Size {
        val ctx = ApplicationContextCompat.getInstance().getApplicationContext()
        val metrics = ctx.resources.displayMetrics
        return Size(metrics.widthPixels, metrics.heightPixels)
    }

    fun onNext(editorViewModel: EditorViewModel) {
        currentTemplate?.let { photo.copyFrom(it) }
        editorViewModel.pushBitmap(bitmap.value)
    }
}