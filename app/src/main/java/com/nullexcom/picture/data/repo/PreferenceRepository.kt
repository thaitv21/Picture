package com.nullexcom.picture.data.repo

import android.content.SharedPreferences
import com.nullexcom.picture.data.PreferenceProvider

class PreferenceRepository () {

    private val sharedPreferences: SharedPreferences by lazy { PreferenceProvider.getAppPreferences() }

    var isFirstUse: Boolean
        get() = sharedPreferences.getBoolean("isFirstUse", true)
        set(value) {
            sharedPreferences.edit().putBoolean("isFirstUse", value).apply()
        }
}