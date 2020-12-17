package com.nullexcom.editor.ext

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap

fun Drawable.toBitmap() : Bitmap{
    return when (this) {
        is BitmapDrawable -> this.bitmap
        else -> this.toBitmap(intrinsicWidth, intrinsicHeight)
    }
}