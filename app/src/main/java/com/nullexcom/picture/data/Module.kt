package com.nullexcom.picture.data

import androidx.renderscript.Allocation
import androidx.renderscript.RenderScript


interface Module {
    fun process(rs: RenderScript, allocationIn: Allocation, allocationOut: Allocation)
}