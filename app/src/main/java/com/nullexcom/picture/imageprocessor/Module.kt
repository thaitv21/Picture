package com.nullexcom.picture.imageprocessor

import androidx.renderscript.Allocation
import androidx.renderscript.RenderScript


interface Module {
    fun process(rs: RenderScript, allocationIn: Allocation, allocationOut: Allocation)
    fun isUseless() : Boolean
}