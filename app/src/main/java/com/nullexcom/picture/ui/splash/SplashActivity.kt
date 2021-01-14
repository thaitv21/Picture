package com.nullexcom.picture.ui.splash

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.nullexcom.picture.HomeActivity
import com.nullexcom.picture.R
import com.nullexcom.picture.ext.navigateAndClearBackStack
import com.nullexcom.picture.ui.login.LoginActivity
import com.nullexcom.picture.ui.welcome.WelcomeActivity
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by lazy { ViewModelProvider.NewInstanceFactory().create(SplashViewModel::class.java) }
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val animation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        animation.duration = 1000
        animation.repeatCount = 1
        animation.interpolator = BounceInterpolator()
        imgLogo.startAnimation(animation)
        disposable = viewModel.getState().doOnSuccess { render(it) }.subscribe()
    }

    private fun render(state: SplashViewModel.State) {
        if (!state.isInternetAvailable) {
            navigateAndClearBackStack(HomeActivity::class.java)
            return
        }

        if (!state.isLoggedIn) {
            navigateAndClearBackStack(LoginActivity::class.java)
            return
        }

        if (state.isFirstUse) {
            navigateAndClearBackStack(WelcomeActivity::class.java)
            return
        }

        navigateAndClearBackStack(HomeActivity::class.java)

    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}