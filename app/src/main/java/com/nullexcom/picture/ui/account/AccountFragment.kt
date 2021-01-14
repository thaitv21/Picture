package com.nullexcom.picture.ui.account

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import com.nullexcom.picture.BuildConfig
import com.nullexcom.picture.R
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {

    private val viewModel: AccountViewModel by viewModels()
    private var disposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposable = viewModel.getUserInfo().doOnNext { render(it) }.subscribe()
        tvVersion.text = "Version ${BuildConfig.VERSION_NAME}"
    }

    override fun onDestroyView() {
        disposable?.dispose()
        super.onDestroyView()
    }

    private fun render(firebaseUser: FirebaseUser?) {
        val user = firebaseUser ?: return
        val context = context ?: return
        user.photoUrl?.let {
            Glide.with(context).load(it).into(imgAvatar)
        }
        tvName.text = user.displayName
    }
}