package com.nullexcom.picture.imageprocessor

class ContrastModule : HistogramModule() {
    var value : Float = 0f

    fun get() : Float {
        return value
    }

    override fun applyTo(module: HistogramModule) {
        if (value == 0f) {
            return
        }
        val t = (1f - value) / 2f
        module.matrix[0][0] *= value
        module.matrix[1][1] *= value
        module.matrix[2][2] *= value
        module.matrix[3][0] += t
        module.matrix[3][1] += t
        module.matrix[3][2] += t
    }

    override fun isUseless(): Boolean {
        return value == 0f
    }
}