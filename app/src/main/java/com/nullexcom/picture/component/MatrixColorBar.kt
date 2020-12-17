package com.nullexcom.editor.component

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.MutableLiveData
import com.nullexcom.editor.ext.setOnProgressChanged
import com.nullexcom.picture.R
import kotlinx.android.synthetic.main.matrix_color_bar.view.*

class MatrixColorBar : FrameLayout {
    private val mValue = MutableLiveData(0)
    private val bias = 100
    private var onValueChangedListener: ((Int) -> Unit)? = null

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        inflate(context, R.layout.matrix_color_bar, this)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MatrixColorBar)
        tvLabel.text = ta.getString(R.styleable.MatrixColorBar_label)
        seekBar.progress = ta.getInt(R.styleable.MatrixColorBar_currentValue, 0) + bias
        mValue.value = ta.getInt(R.styleable.MatrixColorBar_currentValue, 0)
        ta.recycle()

        seekBar.max = 2 * bias
        seekBar.setOnProgressChanged { mValue.postValue(it - bias) }
        mValue.observeForever { onValueChanged(it) }
    }

    private fun onValueChanged(value: Int) {
        tvValue.text = value.toString()
        onValueChangedListener?.invoke(value)
    }

    fun setLabel(label: String) {
        tvLabel.text = label
    }

    fun setValue(value: Int) {
        mValue.postValue(value)
        seekBar.progress = value + bias
    }
}