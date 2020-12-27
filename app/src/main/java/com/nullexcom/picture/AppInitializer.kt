package com.nullexcom.picture

import android.content.Context
import androidx.startup.Initializer
import com.nullexcom.picture.data.DataStoreInitializer

class AppInitializer : Initializer<Unit> {
    override fun create(context: Context) {

    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(
                ApplicationContextCompatInitializer::class.java,
                NetworkIntoInitializer::class.java,
                FirebaseInitializer::class.java,
                DataStoreInitializer::class.java
        )
    }

}