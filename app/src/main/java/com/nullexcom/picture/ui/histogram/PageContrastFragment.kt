package com.nullexcom.picture.ui.histogram

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nullexcom.picture.R
import com.nullexcom.picture.component.MiddleProgressBar
import com.nullexcom.picture.data.Template
import com.nullexcom.picture.imageprocessor.BrightnessModule
import com.nullexcom.picture.imageprocessor.ContrastModule
import com.nullexcom.picture.viewmodels.HistogramViewModel
import kotlinx.android.synthetic.main.page_contrast.*
import kotlin.math.roundToInt

class PageContrastFragment : Fragment() {

    private var onValueChanged: ((Float) -> Unit)? = null
    private val viewModel: HistogramViewModel by viewModels({ requireParentFragment() })

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

    override fun onResume() {
        super.onResume()
        val template = viewModel.getTemplate()
        val contrast = template.modules.find { it is ContrastModule }?.let { it as ContrastModule }?.get() ?: 1f
        if (contrast > 1f) {
            val progress = (contrast - 1f) * 50f
            mpvContrast.setValue(progress.roundToInt())
        } else {
            val progress = (contrast - 1f) * 200f
            mpvContrast.setValue(progress.roundToInt())
        }
    }
}