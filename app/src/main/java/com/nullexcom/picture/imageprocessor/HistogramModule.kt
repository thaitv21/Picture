package com.nullexcom.picture.imageprocessor

import androidx.renderscript.Allocation
import androidx.renderscript.RenderScript
import androidx.renderscript.Element
import androidx.renderscript.Matrix4f
import androidx.renderscript.ScriptIntrinsicColorMatrix

open class HistogramModule : Module {
    val matrix = arrayOf(
        arrayOf(1f, 0f, 0f, 0f),
        arrayOf(0f, 1f, 0f, 0f),
        arrayOf(0f, 0f, 1f, 0f),
        arrayOf(0f, 0f, 0f, 1f)
    )

    open fun applyTo(module: HistogramModule) {

    }

    fun merge(module: HistogramModule) : HistogramModule {
        module.applyTo(this)
        return this
    }

    private fun toMatrix4f() : Matrix4f {
        val values = mutableListOf<Float>()
        matrix.forEach { values.addAll(it) }
        return Matrix4f(values.toFloatArray())
    }

    override fun process(rs: RenderScript, allocationIn: Allocation, allocationOut: Allocation) {
        val script = ScriptIntrinsicColorMatrix.create(rs, Element.U8_4(rs))
        val matrix = toMatrix4f()
        script.setColorMatrix(matrix)
        script.forEach(allocationIn, allocationOut)
    }

    override fun isUseless() : Boolean {
        for (i in 0..3) {
            for (j in 0..3) {
                if (i == j && matrix[i][j] != 1f) return false
                if (i != j && matrix[i][j] != 0f) return false
            }
        }
        return true
    }
}