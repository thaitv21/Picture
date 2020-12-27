package com.nullexcom.picture.ui.histogram

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nullexcom.picture.R
import com.nullexcom.picture.imageprocessor.BrightnessModule
import com.nullexcom.picture.viewmodels.HistogramViewModel
import kotlinx.android.synthetic.main.page_brightness.*
import kotlin.math.roundToInt

class PageBrightnessFragment : Fragment() {

    private var onValueChanged: ((Float) -> Unit)? = null
    private val viewModel: HistogramViewModel by viewModels({ requireParentFragment() })

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

    override fun onResume() {
        super.onResume()
        val template = viewModel.getTemplate()
        val brightness = template.modules.find { it is BrightnessModule }?.let { it as BrightnessModule }?.get() ?: 0f
        val progress = (brightness * 200f).roundToInt()
        pbBrightness.setValue(progress)
    }

    fun setOnValueChanged(onValueChanged: ((Float) -> Unit)?) {
        this.onValueChanged = onValueChanged
    }
}