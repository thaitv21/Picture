package com.nullexcom.picture.data

import android.R.attr.description
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toFile
import com.nullexcom.picture.AppState
import com.nullexcom.picture.ext.emptyMatrix
import com.nullexcom.picture.ext.stringify
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files


class ImageExporter(val bitmap: Bitmap, val uri: Uri, val name: String, private val template: Template) {

    private val context = AppState.application.applicationContext
    private val presetFile: File

    init {
        val dir = context.getExternalFilesDir("presets")
        val imageFile = uri.toFile()
        presetFile = File(dir, "${imageFile.nameWithoutExtension}.preset")
    }

    fun export(): Uri? {
        exportPreset()
        return exportImage()
    }

    private fun exportImage(): Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, name)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, name)
        values.put(MediaStore.Images.Media.DESCRIPTION, description)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        val contentResolver = context.contentResolver
        val insertUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val file = File(context.getExternalFilesDir("out"), uri.toFile().name)
        insertUri?.let {
            contentResolver.openOutputStream(it)?.apply {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
                close()
            }
        }
        return insertUri
    }

    private fun exportPreset() {
        if (!presetFile.exists()) {
            presetFile.createNewFile()
        }
        presetFile.writeBytes(template.toString().toByteArray())
    }
}