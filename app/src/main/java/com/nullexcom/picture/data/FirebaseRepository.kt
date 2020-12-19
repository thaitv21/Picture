package com.nullexcom.picture.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class FirebaseRepository {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mDatabase: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val mStorage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    private val loggedIn = BehaviorSubject.createDefault(mAuth.currentUser != null)

    init {
        mAuth.addAuthStateListener { loggedIn.onNext(it.currentUser != null) }
    }

    fun isLoggedIn() : Observable<Boolean> {
        return loggedIn.distinctUntilChanged()
    }
}