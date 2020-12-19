package com.nullexcom.picture.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DataStorePreferences private constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        private lateinit var instance: DataStorePreferences

        fun initialize(context: Context) {
            instance = DataStorePreferences(context.createDataStore("app"))
        }

        fun getInstance(): DataStorePreferences {
            return instance
        }
    }

    private val firstUse = BehaviorSubject.create<Boolean>()

    fun isFirstUse(): Observable<Boolean> {
        val key = preferencesKey<Boolean>("first_use")
        GlobalScope.launch {
            dataStore.data.map { it[key] ?: true }.onEach { firstUse.onNext(it) }.collect()
        }
        return firstUse
    }

    fun saveFirstUse() {
        val key = preferencesKey<Boolean>("first_use")
        GlobalScope.launch {
            dataStore.edit { it[key] = false }
        }
    }
}