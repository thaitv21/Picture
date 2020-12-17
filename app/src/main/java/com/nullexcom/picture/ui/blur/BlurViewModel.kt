package com.nullexcom.picture.ui.blur

import android.graphics.*
import androidx.renderscript.Allocation
import androidx.renderscript.Element
import androidx.renderscript.ScriptIntrinsicBlur
import com.nullexcom.editor.ext.logD
import com.nullexcom.picture.AppState
import com.nullexcom.picture.data.BlurModule
import com.nullexcom.picture.data.HSLModule
import com.nullexcom.picture.data.Module
import com.panasonic.cdd.imagesegmentationlib.ImageSegmentationModule
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BlurViewModel(val bitmap: Bitmap, val modules: MutableList<Module>) {

    private val blurredBitmap = BehaviorSubject.createDefault(bitmap.copy(Bitmap.Config.ARGB_8888, true))
    private lateinit var segmentedBitmaps: Array<Bitmap?>

    fun getBlurredBitmap(): Observable<Bitmap> {
        return blurredBitmap
    }

    fun onRadiusChanged(radius: Float) {
        var module : BlurModule? = modules.find { it is BlurModule }?.let { it as BlurModule }
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

        // segment
        if (!::segmentedBitmaps.isInitialized) {
            segmentedBitmaps = ImageSegmentationModule.segment(AppState.application, bitmap)
        }

        val mask: Bitmap = segmentedBitmaps[1] ?: return
        CoroutineScope(Dispatchers.Default).launch {
            val outBitmap = blurredBitmap.value
            module.process(AppState.rs, bitmap, mask, outBitmap)
            withContext(Dispatchers.Main) {
                blurredBitmap.onNext(outBitmap)
            }
        }

    }
}