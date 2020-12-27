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
import com.nullexcom.picture.imageprocessor.SaturationModule
import com.nullexcom.picture.viewmodels.HistogramViewModel
import kotlinx.android.synthetic.main.page_saturation.*
import kotlin.math.roundToInt

class PageSaturationFragment : Fragment() {
    private var onValueChanged: ((Float) -> Unit)? = null
    private val viewModel: HistogramViewModel by viewModels({ requireParentFragment() })

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

    override fun onResume() {
        super.onResume()
        val template = viewModel.getTemplate()
        val saturation = template.modules.find { it is SaturationModule }?.let { it as SaturationModule }?.value
                ?: 1f
        val progress = (saturation - 1f) * 100f
        mpvSaturation.setValue(progress.roundToInt())
    }
}