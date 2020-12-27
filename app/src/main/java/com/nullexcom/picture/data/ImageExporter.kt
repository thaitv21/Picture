package com.nullexcom.picture.data

import android.R.attr.description
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Size
import androidx.core.net.toFile
import com.nullexcom.picture.AppState
import com.nullexcom.picture.ApplicationContextCompat
import com.nullexcom.picture.imageprocessor.ImageProcessor
import com.nullexcom.picture.ext.toBitmap
import java.io.File
import java.io.FileOutputStream


class ImageExporter(val photo: Photo, val name: String, private val template: Template) {

    private val context = AppState.application.applicationContext
    private val presetFile: File
    private val uri = photo.uri

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
            val bitmap = applyFilter()
            contentResolver.openOutputStream(it)?.apply {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
                close()
            }
        }
        return insertUri
    }

    private fun applyFilter() : Bitmap {
        val bitmap = uri.toBitmap()
        val size = Size(bitmap.width, bitmap.height)
        val applicationContextCompat = ApplicationContextCompat.getInstance()
        val context = applicationContextCompat.getApplicationContext()
        val imageProcessor = ImageProcessor(context, photo, size)
        val outBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        imageProcessor.blend(outBitmap, template)
        return bitmap
    }


    private fun exportPreset() {
        if (!presetFile.exists()) {
            presetFile.createNewFile()
        }
        presetFile.writeBytes(template.toString().toByteArray())
    }
}