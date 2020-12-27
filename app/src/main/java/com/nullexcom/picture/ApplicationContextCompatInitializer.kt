package com.nullexcom.picture

import android.content.Context
import androidx.startup.Initializer

class ApplicationContextCompatInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        ApplicationContextCompat.initialize(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }

}