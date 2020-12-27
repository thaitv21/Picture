package com.nullexcom.picture.component

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.nullexcom.picture.R
import kotlinx.android.synthetic.main.middle_progress_bar.view.*

class MiddleProgressView : LinearLayout {


    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    fun init(attrs: AttributeSet?) {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.middle_progress_bar, this)
        gravity = Gravity.CENTER
        orientation = VERTICAL
    }

    fun setOnProgressChangedListener(onProgressChanged: MiddleProgressBar.OnProgressChangedListener) {
        pbValue.setOnProgressChangedListener { progress, fromUser ->
            kotlin.run {
                tvValue.text = progress.toString()
                onProgressChanged.onProgressChanged(progress, fromUser)
            }
        }
    }

    fun setValue(value: Int) {
        pbValue.setValue(value)
    }
}