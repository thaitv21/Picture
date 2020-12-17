package com.nullexcom.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.page_brightness.*

class PageBrightnessFragment : Fragment() {

    private var onValueChanged: ((Float) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_brightness, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pbBrightness.setOnProgressChangedListener { progress, fromUser ->
            if (fromUser) {
                tvValue.text = progress.toString()
                val brightness = progress / 200f
                onValueChanged?.invoke(brightness)
            }
        }
    }

    fun setOnValueChanged(onValueChanged: ((Float) -> Unit)?) {
        this.onValueChanged = onValueChanged
    }
}