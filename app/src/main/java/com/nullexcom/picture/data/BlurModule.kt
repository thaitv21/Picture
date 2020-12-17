package com.nullexcom.picture.data

import android.graphics.*
import androidx.renderscript.Allocation
import androidx.renderscript.Element
import androidx.renderscript.RenderScript
import androidx.renderscript.ScriptIntrinsicBlur
import com.nullexcom.editor.ext.logD
import com.nullexcom.picture.AppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BlurModule : Module {

    private var radius = 0f

    fun set(radius: Float) {
        this.radius = radius
    }

    fun get() : Float {
        return radius
    }

    override fun process(rs: RenderScript, allocationIn: Allocation, allocationOut: Allocation) {

    }

    fun process(rs: RenderScript, bitmap: Bitmap, mask: Bitmap, outBitmap: Bitmap) {
        // blur background
        val background = blurBackground(rs, bitmap, outBitmap, radius)

        // create blurred bitmap
        val canvas = Canvas(background)
        val paint = Paint()
        val src = Rect(0, 0, mask.width - 1, mask.height - 1)
        val dst = Rect(0, 0, bitmap.width - 1, bitmap.height - 1)
        canvas.drawBitmap(background, 0f, 0f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)
        canvas.drawBitmap(mask, src, dst, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        logD("draw")
    }

    private fun blurBackground(rs: RenderScript, bitmap: Bitmap, outBitmap: Bitmap, radius: Float): Bitmap {
        val script = ScriptIntrinsicBlur.create(AppState.rs, Element.U8_4(AppState.rs))
        val allocationIn = Allocation.createFromBitmap(rs, bitmap)
        val allocationOut = Allocation.createTyped(rs, allocationIn.type)
        script.setRadius(radius)
        script.setInput(allocationIn)
        script.forEach(allocationOut)
        allocationOut.copyTo(outBitmap)
        return outBitmap
    }

}