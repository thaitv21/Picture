package com.nullexcom.picture.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.core.net.toFile
import com.nullexcom.picture.ApplicationContextCompat
import java.io.File
import kotlin.math.ceil

fun Uri.toBitmap(): Bitmap {
    return toBitmap(calculateImageSize(toFile()))
}

fun Uri.toPreferBitmap(): Bitmap {
    val applicationContextCompat = ApplicationContextCompat.getInstance()
    val size = applicationContextCompat.preferBitmapSize()
    return toBitmap(size)
}

fun Uri.bitmapSize(): Size {
    return calculateImageSize(toFile())
}

fun Size.scaleToFitHeight(reqHeight: Int): Size {
    if (height <= reqHeight) {
        return this
    }
    val scale = ceil(height / reqHeight.toFloat()).toInt()
    val newWidth = width / scale
    val newHeight = height / scale
    return Size(newWidth, newHeight)
}

fun Uri.toBitmap(reqSize: Size): Bitmap {
    val file = this.toFile()
    val size = calculateImageSize(file)
    val options = BitmapFactory.Options()
    options.inPreferredConfig = Bitmap.Config.ARGB_8888
    options.inSampleSize = calculateInSampleSize(size, reqSize)
    return BitmapFactory.decodeFile(file.absolutePath, options)
}

private fun calculateImageSize(file: File): Size {
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeFile(file.absolutePath, options)
    val imageHeight: Int = options.outHeight
    val imageWidth: Int = options.outWidth
    return Size(imageWidth, imageHeight)
}

private fun calculateInSampleSize(size: Size, requestSize: Size): Int {
    val (height: Int, width: Int) = size.run { height to width }
    val (reqHeight: Int, reqWidth) = requestSize.run { requestSize.height to requestSize.width }
    if (reqHeight >= height && reqWidth >= width) {
        return 1
    }
    var inSampleSize = 2
    while (width / inSampleSize > reqWidth && height / inSampleSize > height) {
        inSampleSize *= 2
    }
    return inSampleSize
}