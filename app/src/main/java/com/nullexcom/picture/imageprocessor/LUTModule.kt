package com.nullexcom.picture.imageprocessor

import android.graphics.BitmapFactory
import androidx.renderscript.*
import com.nullexcom.editor.ext.logD
import com.nullexcom.picture.ApplicationContextCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream

class LUTModule(var name: String?) : Module {

    private lateinit var script: ScriptIntrinsic3DLUT

    override fun process(rs: RenderScript, allocationIn: Allocation, allocationOut: Allocation) {
        if (name == null) {
            return
        }
        val s = System.currentTimeMillis()
        if (!::script.isInitialized) {
            script = ScriptIntrinsic3DLUT.create(rs, Element.U8_4(rs))
        }
        val tb: Type.Builder = Type.Builder(rs, Element.U8_4(rs))
        tb.setX(64).setY(64).setZ(64)
        val t: Type = tb.create()
        val allocCube = Allocation.createTyped(rs, t)
        val context = ApplicationContextCompat.getInstance().getApplicationContext()
        val inputStream: InputStream = context.assets.open("luts/$name")
        val lutBitmap = BitmapFactory.decodeStream(inputStream)
        val dim = 64
        val lut = IntArray(64 * 64 * 64)
        val pixels = IntArray(lutBitmap.width * lutBitmap.height)
        lutBitmap.getPixels(pixels, 0, lutBitmap.width, 0, 0, lutBitmap.width, lutBitmap.height)
        var i = 0
        for (r in 0 until dim) {
            for (g in 0 until dim) {
                for (b in 0 until dim) {
                    val x = 64 * (b % 8) + r
                    val y = 64 * (b / 8) + g
                    val index = y * lutBitmap.width + x
                    lut[i++] = pixels[index]
                }
            }
        }
        allocCube.copyFromUnchecked(lut)
        script.setLUT(allocCube)
        script.forEach(allocationIn, allocationOut)
        inputStream.close()
        GlobalScope.launch {
            allocCube.destroy()
            lutBitmap.recycle()
        }
    }

    override fun isUseless(): Boolean {
        return name == null
    }

}