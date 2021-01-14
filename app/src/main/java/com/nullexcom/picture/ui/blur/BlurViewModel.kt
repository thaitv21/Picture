package com.nullexcom.picture.ui.blur

import android.graphics.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.nullexcom.picture.AppState
import com.nullexcom.picture.ApplicationContextCompat
import com.nullexcom.picture.EditorViewModel
import com.nullexcom.picture.imageprocessor.ImageProcessor
import com.nullexcom.picture.data.*
import com.nullexcom.picture.imageprocessor.BlurModule
import com.nullexcom.picture.viewmodels.AppViewModel
import com.panasonic.cdd.imagesegmentationlib.ImageSegmentationModule
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.*

class BlurViewModel(private val photo: Photo, private val srcBitmap: Bitmap) : AppViewModel(), LifecycleObserver {
    private val bitmap: BehaviorSubject<Bitmap> = BehaviorSubject.create()
    private val template = Template().apply { copyFrom(photo.template) }
    private val modules = template.modules
    private val applicationContextCompat: ApplicationContextCompat by lazy { ApplicationContextCompat.getInstance() }
    private lateinit var segmentedBitmaps: Array<Bitmap?>
    private var job : Job? = null
    private val imageProcessor: ImageProcessor by lazy {
        ImageProcessor(applicationContextCompat.getApplicationContext(), photo, applicationContextCompat.preferBitmapSize())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initialize() {
        bitmap.onNext(srcBitmap)
        GlobalScope.launch {
            val bmp = srcBitmap.copy(srcBitmap.config, true)
            withContext(Dispatchers.Main) {
                bitmap.onNext(bmp)
            }
        }
    }

    fun getBlurredBitmap(): Observable<Bitmap> {
        return bitmap
    }

    fun onRadiusChanged(radius: Float) {
        var module: BlurModule? = modules.find { it is BlurModule }?.let { it as BlurModule }
        if (module == null) {
            module = BlurModule()
            modules.add(module)
        }
        module.set(radius)
        apply()
    }

    private fun apply() {
        val module = modules.find { it is BlurModule }?.let { it as BlurModule } ?: return
        val radius = module.get()
        if (radius == 0f) return
        job?.cancel()
        job = CoroutineScope(Dispatchers.Default).launch {
            // segment
            if (!::segmentedBitmaps.isInitialized) {
                segmentedBitmaps = ImageSegmentationModule.segment(applicationContextCompat.getApplicationContext(), srcBitmap)
            }

            val mask: Bitmap = segmentedBitmaps[1] ?: return@launch
            val outBitmap = bitmap.value
            module.process(AppState.rs, srcBitmap, mask, outBitmap)
            withContext(Dispatchers.Main) {
                bitmap.onNext(outBitmap)
            }
        }
    }

    override fun onCleared() {
//        segmentedBitmaps.forEach { it?.recycle() }
//        if (bitmap.value != srcBitmap) {
//            bitmap.value.recycle()
//        }
        super.onCleared()
    }

    fun onBack(editorViewModel: EditorViewModel) {
        editorViewModel.popBitmap()
    }
}