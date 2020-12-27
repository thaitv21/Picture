package com.nullexcom.picture

import android.content.Context
import android.util.Size
import com.nullexcom.picture.ext.dp
import java.io.File

class ApplicationContextCompat(private val applicationContext: Context) {
    companion object {
        private lateinit var instance: ApplicationContextCompat

        fun initialize(context: Context) {
            instance = ApplicationContextCompat(context.applicationContext)
        }

        fun getInstance(): ApplicationContextCompat {
            return instance
        }
    }

    fun getExternalFilesDir(dir: String?) : File? {
        return applicationContext.getExternalFilesDir(dir)
    }

    fun getApplicationContext(): Context {
        return applicationContext
    }

    fun dp(dp: Float) : Int {
        return applicationContext.dp(dp)
    }

    fun preferBitmapSize(): Size {
        val metrics = applicationContext.resources.displayMetrics
        return Size(metrics.widthPixels, metrics.heightPixels)
    }
}