package com.nullexcom.picture

import android.graphics.Bitmap
import androidx.renderscript.*
import com.nullexcom.editor.ext.logD
import com.nullexcom.picture.data.*
import com.nullexcom.picture.ext.Matrix
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistogramViewModel(val croppedBitmap: Bitmap, val modules: MutableList<Module>) {
    val filteredBitmap: BehaviorSubject<Bitmap> = BehaviorSubject.createDefault(croppedBitmap.copy(Bitmap.Config.ARGB_8888, true))
    private val allocationIn: Allocation by lazy { Allocation.createFromBitmap(AppState.rs, croppedBitmap) }
    private val allocationOut: Allocation by lazy { Allocation.createTyped(AppState.rs, allocationIn.type) }

    init {
        apply()
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
            val rs = AppState.rs

            val bitmap = filteredBitmap.value

            //handle color matrix module
            val module = HistogramModule()
            modules.forEach { if (it is HistogramModule) module.merge(it) }
            module.process(rs, allocationIn, allocationOut)

            //handle HSL
            val hslModule = modules.find { it is HSLModule }
            hslModule?.process(rs, allocationOut, allocationOut)

            allocationOut.copyTo(bitmap)

            withContext(Dispatchers.Main) {
                logD("finish")
                filteredBitmap.onNext(bitmap)
            }
        }
    }
}