package com.nullexcom.picture.data.repo

import com.google.firebase.auth.FirebaseAuth

class FirebaseRepository {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    fun isLoggedIn(): Boolean {
        return mAuth.currentUser != null
    }
}