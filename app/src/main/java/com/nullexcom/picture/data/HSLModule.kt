package com.nullexcom.picture.data

import androidx.renderscript.Allocation
import androidx.renderscript.Element
import androidx.renderscript.RenderScript
import com.nullexcom.picture.ScriptC_hsl
import com.nullexcom.picture.ext.copyFrom

class HSLModule : Module {

    private val values = FloatArray(36) { 0f }

    fun set(index: Int, value: Float) {
        values[index] = value
    }

    fun set(values: FloatArray) {
        this.values.copyFrom(values)
    }

    fun get(): FloatArray {
        return values
    }

    override fun process(rs: RenderScript, allocationIn: Allocation, allocationOut: Allocation) {
        val script = ScriptC_hsl(rs)
        val allocationValues = Allocation.createSized(rs, Element.F32(rs), values.size)
        allocationValues.copyFrom(values)
        script.bind_values(allocationValues)
        script.forEach_process(allocationIn, allocationOut)
    }
}