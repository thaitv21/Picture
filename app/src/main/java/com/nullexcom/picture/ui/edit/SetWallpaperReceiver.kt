package com.nullexcom.picture.ui.edit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class SetWallpaperReceiver : BroadcastReceiver() {

    private var onWallpaperChanged: (() -> Unit)? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        onWallpaperChanged?.invoke()
    }

    fun setOnWallpaperChanged( onWallpaperChanged: (() -> Unit)?) {
        this.onWallpaperChanged = onWallpaperChanged
    }
}