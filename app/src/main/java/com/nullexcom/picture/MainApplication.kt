package com.nullexcom.picture

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
public class MainApplication : Application() {
    companion object {
        lateinit var application: MainApplication
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        Strings.loadResources(this)
        AppState.application = this
    }
}