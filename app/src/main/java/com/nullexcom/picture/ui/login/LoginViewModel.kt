package com.nullexcom.picture.ui.login

import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.nullexcom.picture.AppState
import com.nullexcom.picture.R
import com.nullexcom.picture.data.repo.PreferenceRepository
import com.nullexcom.picture.ext.getString
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class LoginViewModel : ViewModel() {

    private val preferenceRepository: PreferenceRepository by lazy { PreferenceRepository() }
    private val state = BehaviorSubject.create<LoginState>()

    fun getState(): Observable<LoginState> {
        return state
    }

    fun signInWithGoogle(activity: LoginActivity) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).apply {
            requestIdToken(activity.getString(R.string.default_web_client_id))
            requestEmail()
            requestProfile()
            requestId()
        }.build()
        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, LoginActivity.RC_GOOGLE_SIGN_IN)
    }


    fun firebaseAuthWithGoogle(intent: Intent?, auth: FirebaseAuth) {
        val data = intent ?: return
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val idToken = account.idToken
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener {
                val isFirstUse = preferenceRepository.isFirstUse
                val nextState = if (it.isSuccessful) LoginState.Successful(isFirstUse) else LoginState.Error("Authentication failed.")
                state.onNext(nextState)
            }
        } catch (e: ApiException) {
            state.onNext(LoginState.Error(e.message ?: getString(R.string.error_sign_in_unknown)))
        }
    }
}