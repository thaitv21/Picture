package com.nullexcom.picture.data

import com.nullexcom.picture.data.HistogramModule

class ContrastModule : HistogramModule() {
    var value : Float = 0f

    override fun applyTo(module: HistogramModule) {
        val t = (1f - value) / 2f
        module.matrix[0][0] *= value
        module.matrix[1][1] *= value
        module.matrix[2][2] *= value
        module.matrix[3][0] += t
        module.matrix[3][1] += t
        module.matrix[3][2] += t
    }
}