package com.nullexcom.picture.ui.login

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.renderscript.Allocation
import androidx.renderscript.Element
import androidx.renderscript.RenderScript
import androidx.renderscript.ScriptIntrinsicBlur
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.nullexcom.picture.HomeActivity
import com.nullexcom.picture.R
import com.nullexcom.picture.ext.alert
import com.nullexcom.picture.ext.navigateAndClearBackStack
import com.nullexcom.picture.ui.dialog.LoadingDialog
import com.nullexcom.picture.ui.welcome.WelcomeActivity
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        const val RC_GOOGLE_SIGN_IN = 1
    }

    private lateinit var mInAllocation: Allocation
    private lateinit var mOutAllocation: Allocation
    private lateinit var mSourceBitmap: Bitmap
    private lateinit var mOutputBitmap: Bitmap
    private lateinit var script: ScriptIntrinsicBlur
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: LoginViewModel
    private lateinit var disposable: Disposable
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initBackground()
        initViews()
        auth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider.NewInstanceFactory().create(LoginViewModel::class.java)
        disposable = viewModel.getState().doOnNext { renderState(it) }.subscribe()
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    private fun renderState(state: LoginState) {
        if (state is LoginState.Authenticating) {
            loadingDialog.show()
            return
        }
        loadingDialog.cancel()
        when (state) {
            is LoginState.Error -> onError(state.reason)
            is LoginState.Successful -> onSuccess(state.isFirstUse)
        }
    }

    private fun initBackground() {
        val rs = RenderScript.create(this)
        mSourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.background)
        mOutputBitmap = Bitmap.createBitmap(mSourceBitmap)
        mInAllocation = Allocation.createFromBitmap(rs, mSourceBitmap)
        mOutAllocation = Allocation.createFromBitmap(rs, mOutputBitmap)
        script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        script.setInput(mInAllocation)
        script.setRadius(10f)
        script.forEach(mOutAllocation)
        mOutAllocation.copyTo(mOutputBitmap)
        imgBackground.setImageBitmap(mOutputBitmap)
    }

    private fun initViews() {
        loadingDialog = LoadingDialog(this)
        btnSignInGoogle.setOnClickListener { viewModel.signInWithGoogle(this) }
        btnSignInFacebook.setOnClickListener { }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_GOOGLE_SIGN_IN -> viewModel.firebaseAuthWithGoogle(data, auth)
        }
    }

    private fun onError(message: String) {
        alert("Oops", message)
        loadingDialog.cancel()
    }

    private fun onSuccess(isFirstUse: Boolean) {
        if (isFirstUse) {
            navigateAndClearBackStack(WelcomeActivity::class.java)
            return
        }
        navigateAndClearBackStack(HomeActivity::class.java)
    }
}
