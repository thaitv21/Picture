package com.nullexcom.picture.imageprocessor

class BrightnessModule : HistogramModule() {

    var value: Float = 0.0f

    fun get() : Float {
        return value
    }

    override fun applyTo(module: HistogramModule) {
        if (value == 0f) {
            return
        }
        module.matrix[3][0] += value
        module.matrix[3][1] += value
        module.matrix[3][2] += value
    }

    override fun isUseless(): Boolean {
        return value == 0f
    }
}