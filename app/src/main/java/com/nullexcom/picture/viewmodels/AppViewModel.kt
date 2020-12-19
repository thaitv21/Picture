package com.nullexcom.picture.viewmodels

import androidx.lifecycle.ViewModel
import com.nullexcom.picture.data.FirebaseRepository
import com.nullexcom.picture.data.NetworkInfo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import kotlin.math.log

open class AppViewModel : ViewModel() {

    protected val firebaseRepository: FirebaseRepository by lazy { FirebaseRepository() }
    protected val networkInfo: NetworkInfo by lazy { NetworkInfo.getInstance() }

    fun isInternetAvailable(): Observable<Boolean> {
        return networkInfo.isConnectionAvailable()
    }

    fun isLoggedIn(): Observable<Boolean> {
        return firebaseRepository.isLoggedIn()
    }

    fun isUserOnline(): Observable<Boolean> {
        return Observable.combineLatest(isInternetAvailable(), isLoggedIn(), BiFunction { internetAvailable, loggedIn ->
            internetAvailable && loggedIn
        })
    }
}