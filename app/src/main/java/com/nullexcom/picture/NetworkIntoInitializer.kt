package com.nullexcom.picture

import android.content.Context
import androidx.startup.Initializer
import com.nullexcom.picture.data.NetworkInfo

class NetworkIntoInitializer : Initializer<NetworkInfo> {
    override fun create(context: Context): NetworkInfo {
        NetworkInfo.initialize(context)
        return NetworkInfo.getInstance()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}