package com.nullexcom.picture.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.nullexcom.picture.ApplicationContextCompat
import com.nullexcom.picture.EditorViewModel
import com.nullexcom.picture.data.*
import com.nullexcom.picture.ext.Matrix
import com.nullexcom.picture.imageprocessor.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.*

class HistogramViewModel(val photo: Photo, private val srcBitmap: Bitmap) : AppViewModel(), LifecycleObserver {
    private val bitmap: BehaviorSubject<Bitmap> = BehaviorSubject.create()
    private val template = Template().apply { copyFrom(photo.template) }
    private val modules = template.modules
    private val applicationContextCompat: ApplicationContextCompat by lazy { ApplicationContextCompat.getInstance() }
    private val imageProcessor: ImageProcessor by lazy {
        ImageProcessor(applicationContextCompat.getApplicationContext(), photo, applicationContextCompat.preferBitmapSize())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initialize() {
        bitmap.onNext(srcBitmap)
    }

    fun getTemplate() : Template {
        return template
    }

    fun getBitmap(): Observable<Bitmap> {
        return bitmap
    }

    fun onNext(editorViewModel: EditorViewModel) {
        photo.copyFrom(template)
        editorViewModel.pushBitmap(bitmap.value)
    }

    fun onBack(editorViewModel: EditorViewModel) {
        editorViewModel.popBitmap()
    }

    fun onBrightnessChanged(value: Float) {
        var module = modules.find { it is BrightnessModule }
        if (module == null) {
            module = BrightnessModule()
            modules.add(module)
        }
        (module as BrightnessModule).value = value
        apply()
    }

    fun onContrastChanged(value: Float) {
        var module = modules.find { it is ContrastModule }
        if (module == null) {
            module = ContrastModule()
            modules.add(module)
        }
        (module as ContrastModule).value = value
        apply()
    }

    fun onSaturationChanged(value: Float) {
        var module = modules.find { it is SaturationModule }
        if (module == null) {
            module = SaturationModule()
            modules.add(module)
        }
        (module as SaturationModule).value = value
        apply()
    }

    fun onColorMatrixChanged(matrix: Matrix) {
        var module = modules.find { it is ColorMatrixModule }
        if (module == null) {
            module = ColorMatrixModule()
            modules.add(module)
        }
        (module as ColorMatrixModule).setMatrix(matrix)
        apply()
    }

    fun onHSLChanged(values: FloatArray) {
        var module = modules.find { it is HSLModule }
        if (module == null) {
            module = HSLModule()
            modules.add(module)
        }
        (module as HSLModule).set(values)
        apply()
    }

    private fun apply() {
        CoroutineScope(Dispatchers.Default).launch {
            val bmp = bitmap.value
            GlobalScope.launch {
                imageProcessor.blend(bmp, template)
                withContext(Dispatchers.Main) {
                    bitmap.onNext(bmp)
                }
            }
        }
    }
}