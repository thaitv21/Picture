package com.nullexcom.picture.ui.welcome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nullexcom.picture.HomeActivity
import com.nullexcom.picture.R
import com.nullexcom.picture.data.repo.PreferenceRepository
import com.nullexcom.picture.ext.navigateAndClearBackStack
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        btnStart.setOnClickListener { onClickStart() }
    }

    private fun onClickStart() {
        PreferenceRepository().isFirstUse = true
        navigateAndClearBackStack(HomeActivity::class.java)
    }
}