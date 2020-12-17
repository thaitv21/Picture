package com.nullexcom.picture.data

import com.nullexcom.picture.ext.matrixOf
import com.nullexcom.picture.ext.multiply

class SaturationModule : HistogramModule() {
    var value: Float = 0f
    private val lumR = 0.3086f
    private val lumG = 0.6094f
    private val lumB = 0.0820f

    override fun applyTo(module: HistogramModule) {
        val sr = (1 - value) * lumR
        val sg = (1 - value) * lumG
        val sb = (1 - value) * lumB
        val matrixA = matrixOf(
            module.matrix[0][0], module.matrix[0][1], module.matrix[0][2],
            module.matrix[1][0], module.matrix[1][1], module.matrix[1][2],
            module.matrix[2][0], module.matrix[2][1], module.matrix[2][2]
        )
        val matrixB = matrixOf(
            sr + value, sr, sr,
            sg, sg + value, sg,
            sb, sb, sb + value
        )
        val result = matrixA.multiply(matrixB)
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                module.matrix[i][j] = result[i][j]
            }
        }
    }
}