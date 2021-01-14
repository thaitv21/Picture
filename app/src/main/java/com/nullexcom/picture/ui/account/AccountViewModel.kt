package com.nullexcom.picture.ui.account

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class AccountViewModel : ViewModel() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val userInfo = BehaviorSubject.create<FirebaseUser?>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initialize() {
        userInfo.onNext(auth.currentUser)
        auth.addAuthStateListener { userInfo.onNext(it.currentUser) }
    }

    fun getUserInfo(): Observable<FirebaseUser?> {
        return userInfo
    }
}