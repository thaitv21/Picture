package com.nullexcom.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nullexcom.picture.component.MiddleProgressBar
import kotlinx.android.synthetic.main.page_saturation.*

class PageSaturationFragment : Fragment() {
    private var onValueChanged: ((Float) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_saturation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mpvSaturation.setOnProgressChangedListener(MiddleProgressBar.OnProgressChangedListener { progress: Int, fromUser: Boolean ->
            if (fromUser) {
                val value = (progress / 100f) + 1f
                onValueChanged?.invoke(value)
            }
        })
    }

    fun setOnValueChanged(onValueChanged: ((Float) -> Unit)?) {
        this.onValueChanged = onValueChanged
    }
}