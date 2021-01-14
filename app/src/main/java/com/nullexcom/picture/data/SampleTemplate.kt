package com.nullexcom.picture.data

import com.nullexcom.picture.ext.matrixOf
import com.nullexcom.picture.imageprocessor.ColorMatrixModule
import com.nullexcom.picture.imageprocessor.LUTModule

object SampleTemplate {
    val Default = Template()
    val BlackAndWhite: Template = Template().apply {
        addModule(ColorMatrixModule().apply {
            setMatrix(matrixOf(
                    0f, 0f, 0f, 0f,
                    1f, 1f, 1f, 0f,
                    0f, 0f, 0f, 0f,
                    0f, 0f, 0f, 1f
            ))
        })
    }
    val Sepium = Template().apply {
        addModule(ColorMatrixModule().apply {
            setMatrix(matrixOf(
                    1.3f, 0f, 0f, 0f,
                    -0.3f, 1.3f, 0f, 0f,
                    1.1f, 0.2f, 0.8f, 0f,
                    0f, 0f, 0f, 1f
            ))
        })
    }
    val Purple = Template().apply {
        addModule(ColorMatrixModule().apply {
            setMatrix(matrixOf(
                    1f, 0f, 0f, 0f,
                    -0.2f, 1f, 1.2f, 0f,
                    0f, 0f, 1f, 1.7f,
                    0f, -0.1f, 0.1f, 1f
            ))
        })
    }
    val Yellow = Template().apply {
        addModule(ColorMatrixModule().apply {
            setMatrix(matrixOf(
                    1f, -0.2f, -0.1f, 0f,
                    0f, 1.00f, 0.00f, 0f,
                    0f, 0.30f, 1.00f, 0f,
                    0f, 0.10f, 0.00f, 1f
            ))
        })
    }
    val Ancient = Template().apply { addModule(LUTModule("lut_ancient.png")) }
    val Bleached = Template().apply { addModule(LUTModule("lut_bleached.png")) }
}