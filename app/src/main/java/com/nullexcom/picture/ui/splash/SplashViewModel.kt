package com.nullexcom.picture.ui.splash

import com.nullexcom.editor.ext.logD
import com.nullexcom.picture.data.DataStorePreferences
import com.nullexcom.picture.viewmodels.AppViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Function3
import java.util.concurrent.TimeUnit

class SplashViewModel : AppViewModel() {

    data class State(
            val isLoggedIn: Boolean,
            val isFirstUse: Boolean,
            val isInternetAvailable: Boolean
    )


    private val dataStore: DataStorePreferences by lazy { DataStorePreferences.getInstance() }

    fun getState(): Single<State> {
        val isLoggedIn = isLoggedIn()
        val isFirstUse = dataStore.isFirstUse()
        val isConnectionAvailable = networkInfo.isConnectionAvailable()
        val function = Function3<Boolean, Boolean, Boolean, State> { loggedIn, firstUse, connectionAvailable -> State(loggedIn, firstUse, connectionAvailable) }
        val defaultState = State(isLoggedIn = false, isFirstUse = true, isInternetAvailable = false)

        isLoggedIn.doOnNext { logD("logged in $it") }.subscribe()
        isFirstUse.doOnNext { logD("first use $it") }.subscribe()
        isConnectionAvailable.doOnNext { logD("isConnectionAvailable $it") }.subscribe()

        return Observable.combineLatest(isLoggedIn, isFirstUse, isConnectionAvailable, function).first(defaultState)
                .delay(2, TimeUnit.SECONDS)
    }

    fun getCurrentNetworkAvailable(): Boolean {
        return networkInfo.isInternetAvailable()
    }
}