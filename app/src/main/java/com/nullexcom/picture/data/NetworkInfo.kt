package com.nullexcom.picture.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.lang.Exception
import java.net.InetAddress
import java.net.UnknownHostException

class NetworkInfo private constructor(cm: ConnectivityManager) {

    companion object {
        private lateinit var instance: NetworkInfo
        fun initialize(context: Context) {
            val cm = context.getSystemService(ConnectivityManager::class.java) ?: return
            instance = NetworkInfo(cm)
        }

        fun getInstance(): NetworkInfo {
            return instance
        }
    }

    private val connectionAvailable = BehaviorSubject.create<Boolean>()
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            connectionAvailable.onNext(isInternetAvailable())
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            connectionAvailable.onNext(false)
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            connectionAvailable.onNext(isInternetAvailable())
        }
    }

    init {
        val networkRequest = NetworkRequest.Builder().apply {
            addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }.build()
        cm.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun isConnectionAvailable() : Observable<Boolean> {
        return connectionAvailable.distinctUntilChanged()
    }

    fun isInternetAvailable(): Boolean {
        try {
            InetAddress.getByName("www.google.com")
            return true
        } catch (e: Exception) {
        }
        return false
    }
}