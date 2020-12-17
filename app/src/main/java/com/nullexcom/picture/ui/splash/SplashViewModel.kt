package com.nullexcom.picture.ui.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import com.nullexcom.picture.AppState
import com.nullexcom.picture.data.repo.FirebaseRepository
import com.nullexcom.picture.data.repo.PreferenceRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

class SplashViewModel : ViewModel() {

    private val preferenceRepository: PreferenceRepository by lazy {
        PreferenceRepository()
    }

    private val firebaseRepository: FirebaseRepository by lazy {
        FirebaseRepository()
    }

    fun getInfo(): Single<Pair<Boolean, Boolean>> {
        return Observable.defer {
            val isFirstUse = preferenceRepository.isFirstUse
            val isLoggedIn = firebaseRepository.isLoggedIn()
            val pair = Pair(isFirstUse, isLoggedIn)
            Observable.just(pair)
        }.single(Pair(first = true, second = false)).delay(2, TimeUnit.SECONDS)
    }
}