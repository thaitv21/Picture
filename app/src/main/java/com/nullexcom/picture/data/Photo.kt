package com.nullexcom.picture.data

import android.net.Uri
import androidx.core.net.toFile
import com.nullexcom.picture.ApplicationContextCompat
import com.nullexcom.picture.data.Template
import java.io.File

data class Photo(val name: String, val uri: Uri, val template: Template = Template()) {
    companion object {
        fun fromFile(imageFile: File): Photo {
            val uri = Uri.fromFile(imageFile)
            val dir = ApplicationContextCompat.getInstance().getExternalFilesDir("presets")
            val presetFile = File(dir, "${imageFile.nameWithoutExtension}.preset")
            if (!presetFile.exists()) {
                return Photo(imageFile.nameWithoutExtension, uri)
            }
            val template = Template.parse(presetFile.readText())
            return Photo(imageFile.nameWithoutExtension, uri, template)
        }

        fun fromUri(uri: Uri): Photo {
            return fromFile(uri.toFile())
        }
    }

    fun copyFrom(template: Template) {
        this.template.copyFrom(template)
    }
}