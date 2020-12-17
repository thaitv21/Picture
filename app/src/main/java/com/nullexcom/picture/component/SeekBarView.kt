package com.nullexcom.editor.component

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.lifecycle.MutableLiveData
import com.nullexcom.picture.R
import kotlinx.android.synthetic.main.seek_bar_view.view.*

class SeekBarView : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private lateinit var title: String
    private var currentValue: Int = 0
    private var maxValue = 100
    private var bias = 0
    private val mProgress = MutableLiveData(0)
    private var onValueChangedListener: ((Int) -> Unit)? = null
    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            inflate(context, R.layout.seek_bar_view, this)
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SeekBarView)
            title = typedArray.getString(R.styleable.SeekBarView_title) ?: ""
            currentValue = typedArray.getInt(R.styleable.SeekBarView_currentValue, 0)
            maxValue = typedArray.getInt(R.styleable.SeekBarView_maxValue, 100)
            bias = typedArray.getInt(R.styleable.SeekBarView_bias, 0)
            typedArray.recycle()

            tvValue.text = currentValue.toString()
            sbValue.max = maxValue
            sbValue.progress = currentValue
        }
        sbValue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mProgress.value = progress - bias
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        mProgress.observeForever {
            currentValue = it
            tvValue.text = currentValue.toString()
            onValueChangedListener?.invoke(currentValue)
        }
    }

    fun setOnValueChangedListener(l: (progress: Int) -> Unit) {
        onValueChangedListener = l
    }

}