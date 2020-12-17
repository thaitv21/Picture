package com.nullexcom.picture.data

import com.nullexcom.picture.data.HistogramModule

class BrightnessModule : HistogramModule() {

    var value: Float = 0.0f

    override fun applyTo(module: HistogramModule) {
        module.matrix[3][0] += value
        module.matrix[3][1] += value
        module.matrix[3][2] += value
    }
}