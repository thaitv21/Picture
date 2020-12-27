package com.nullexcom.picture.imageprocessor

import com.nullexcom.picture.ext.Matrix
import com.nullexcom.picture.ext.copyFrom
import com.nullexcom.picture.ext.matrixOf
import com.nullexcom.picture.ext.multiply

class ColorMatrixModule : HistogramModule() {

    fun setMatrix(matrix: Matrix) {
        this.matrix.copyFrom(matrix)
    }

    override fun applyTo(module: HistogramModule) {
        val matrixA = matrixOf(
                module.matrix[0][0], module.matrix[0][1], module.matrix[0][2],
                module.matrix[1][0], module.matrix[1][1], module.matrix[1][2],
                module.matrix[2][0], module.matrix[2][1], module.matrix[2][2]
        )
        val matrixB = matrixOf(
                matrix[0][0], matrix[0][1], matrix[0][2],
                matrix[1][0], matrix[1][1], matrix[1][2],
                matrix[2][0], matrix[2][1], matrix[2][2]
        )
        val result = matrixA.multiply(matrixB)
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                module.matrix[i][j] = result[i][j]
            }
        }
    }
}