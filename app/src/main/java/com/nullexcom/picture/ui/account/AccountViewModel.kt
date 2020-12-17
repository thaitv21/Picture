package com.nullexcom.picture.ui.account

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class AccountViewModel : ViewModel() {
    private val userInfo = BehaviorSubject.createDefault(FirebaseAuth.getInstance().currentUser)

    fun getUserInfo() : Observable<FirebaseUser?> {
        return userInfo
    }
}