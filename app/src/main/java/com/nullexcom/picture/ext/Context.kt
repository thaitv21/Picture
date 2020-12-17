package com.nullexcom.editor.ext

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import java.io.*


fun Context.copyTo(uri: Uri, file: File) {
    var inputStream: InputStream? = null
    var outputStream: OutputStream? = null
    try {
        if (!file.exists()) file.createNewFile()
        inputStream = contentResolver.openInputStream(uri) ?: return
        outputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var len: Int
        while (inputStream.read(buffer).also { len = it } != -1) {
            outputStream.write(buffer, 0, len)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        inputStream?.close()
        outputStream?.close()
    }
}

val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

fun Context.newImageView() : ImageView = ImageView(this)
fun Context.newTextView() = TextView(this)