package com.nullexcom.picture.imageprocessor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Size
import android.widget.ImageView
import androidx.renderscript.Allocation
import androidx.renderscript.RenderScript
import com.nullexcom.editor.ext.logD
import com.nullexcom.picture.AppState
import com.nullexcom.picture.data.*
import com.nullexcom.picture.ext.toBitmap
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ImageProcessor(context: Context, photo: Photo, size: Size) {
    private val rs: RenderScript by lazy { RenderScript.create(context) }
    private val bitmap: Bitmap by lazy { photo.uri.toBitmap(size) }

    fun blend(out: Bitmap, template: Template, useBlur: Boolean = false) {
        val s = System.currentTimeMillis()
        val modules = template.modules.filter { !it.isUseless() }
        val histogramModule = HistogramModule()
        val allocationIn = Allocation.createFromBitmap(AppState.rs, bitmap)
        val allocationOut = Allocation.createTyped(AppState.rs, allocationIn.type)

        // Skip apply filter if no modules
        if (modules.isEmpty()) {
            allocationIn.copyTo(out)
            val e = System.currentTimeMillis()
            logD("blend: ${e - s}")
            return
        }

        // Histogram module
        modules.forEach {
            if (it is HistogramModule) histogramModule.merge(it)
        }
        if (!histogramModule.isUseless()) {
            histogramModule.process(rs, allocationIn, allocationOut)
        }

        // HSL module
        val hslModule = modules.find { it is HSLModule }
        hslModule?.process(rs, allocationOut, allocationOut)

        // blur module
        val blurModule = modules.find { it is BlurModule }
        blurModule?.process(rs, allocationOut, allocationOut)

        allocationOut.copyTo(out)
        GlobalScope.launch {
            allocationIn.destroy()
            allocationOut.destroy()
        }
        val e = System.currentTimeMillis()
        logD("blend: ${e - s}")
    }

    fun blend(imageView: ImageView, template: Template) {
        val out = if (imageView.drawable is BitmapDrawable) {
            (imageView.drawable as BitmapDrawable).bitmap
        } else {
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        }
        blend(out, template)
        imageView.setImageBitmap(out)
    }
}