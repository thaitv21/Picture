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
import com.nullexcom.picture.ext.ifNotNull
import com.nullexcom.picture.ext.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageProcessor(context: Context, photo: Photo, val size: Size) {
    private val rs: RenderScript by lazy { RenderScript.create(context) }
    private val bitmap: Bitmap by lazy { photo.uri.toBitmap(size) }

    fun blend(bitmap: Bitmap, out: Bitmap, template: Template, useBlur: Boolean = false) {
        val s = System.currentTimeMillis()
        val modules = template.modules.filter { !it.isUseless() }
        val allocationIn = Allocation.createFromBitmap(AppState.rs, bitmap)
        val allocationOut = Allocation.createTyped(AppState.rs, allocationIn.type)

        // Skip apply filter if no modules
        if (modules.isEmpty()) {
            allocationIn.copyTo(out)
            val e = System.currentTimeMillis()
            logD("blend: ${e - s}")
            return
        }

        var tmp = allocationIn

        // LUT
        modules.find { it is LUTModule }?.let { it as LUTModule }.ifNotNull {
            it.process(rs, tmp, allocationOut)
            tmp = allocationOut
        }

        optimizeHistogramModule(modules)?.ifNotNull {
            it.process(rs, tmp, allocationOut)
            tmp = allocationOut
        }

        modules.find { it is HSLModule }?.ifNotNull {
            it.process(rs, tmp, allocationOut)
            tmp = allocationOut
        }

        if (useBlur) {
            modules.find { it is BlurModule }?.let { it as BlurModule }.ifNotNull {
                it.process(rs, allocationOut, allocationOut)
                tmp = allocationOut
            }
        }
        tmp.copyTo(out)
        GlobalScope.launch {
            allocationIn.destroy()
            allocationOut.destroy()
        }
        val e = System.currentTimeMillis()
        logD("blend: ${e - s} with ${modules.size} layers")
    }

    fun blend(out: Bitmap, template: Template, useBlur: Boolean = false) {
        blend(bitmap, out, template, useBlur)
    }

    fun blend(imageView: ImageView, template: Template) {
        val out = if (imageView.drawable is BitmapDrawable) {
            (imageView.drawable as BitmapDrawable).bitmap
        } else {
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        }
//        GlobalScope.launch {
        blend(out, template)
//            withContext(Dispatchers.Main) {
        imageView.setImageBitmap(out)
//            }
//        }
    }

    private fun optimizeHistogramModule(modules: List<Module>): HistogramModule? {
        val histogramModule = HistogramModule()
        modules.forEach {
            if (it is HistogramModule) histogramModule.merge(it)
        }
        return if (histogramModule.isUseless()) {
            null
        } else {
            histogramModule
        }
    }
}