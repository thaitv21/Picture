package com.nullexcom.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nullexcom.picture.component.MiddleProgressBar
import kotlinx.android.synthetic.main.page_brightness.*
import kotlinx.android.synthetic.main.page_contrast.*

class PageContrastFragment : Fragment() {

    private var onValueChanged: ((Float) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_contrast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mpvContrast.setOnProgressChangedListener(MiddleProgressBar.OnProgressChangedListener { progress, fromUser ->
            if (fromUser) {
                val brightness = if (progress > 0) {
                    (2 * progress / 100f) + 1f
                } else {
                    (progress / 200f) + 1f
                }
                onValueChanged?.invoke(brightness)
            }
        })
    }

    fun setOnValueChanged(onValueChanged: ((Float) -> Unit)?) {
        this.onValueChanged = onValueChanged
    }
}