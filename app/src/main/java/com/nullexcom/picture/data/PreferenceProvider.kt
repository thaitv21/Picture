package com.nullexcom.picture.data

import android.content.Context
import android.content.SharedPreferences
import com.nullexcom.picture.AppState

class PreferenceProvider {
    companion object {
        fun getAppPreferences(): SharedPreferences {
            return AppState.application.getSharedPreferences("picture", Context.MODE_PRIVATE)
        }
    }
}