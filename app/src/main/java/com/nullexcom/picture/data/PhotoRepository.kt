package com.nullexcom.picture.data

import android.net.Uri
import com.nullexcom.editor.data.Photo
import com.nullexcom.editor.ext.copyTo
import com.nullexcom.picture.AppState
import java.io.File
import javax.inject.Inject

class PhotoRepository @Inject constructor() {
    private val context = AppState.application.applicationContext
    private val dir = File(context.getExternalFilesDir(null), "images")

    fun photos(): List<Photo> {
        dir.mkdirs()
        val files = dir.listFiles() ?: return emptyList()
        return files.filter { it.isFile }.map { Photo(it.nameWithoutExtension, Uri.fromFile(it)) }
    }

    fun addPhoto(uri: Uri): File {
        val filename = "${System.currentTimeMillis()}.png"
        val file = File(dir, filename)
        context.copyTo(uri, file)
        return file
    }
}