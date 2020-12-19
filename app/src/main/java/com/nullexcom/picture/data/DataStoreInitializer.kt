package com.nullexcom.picture.data

import android.content.Context
import androidx.startup.Initializer

class DataStoreInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        DataStorePreferences.initialize(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }

}