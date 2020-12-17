package com.nullexcom.picture.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.nullexcom.picture.R

class ColorView : androidx.appcompat.widget.AppCompatImageView {

    private val drawable = GradientDrawable()

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {init(attrs)}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {init(attrs)}

    fun init(attrs: AttributeSet?) {
        if (attrs == null) return
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ColorView)
        val startColor = ta.getColor(R.styleable.ColorView_android_startColor, Color.WHITE)
        val endColor = ta.getColor(R.styleable.ColorView_android_endColor, Color.BLACK)
        drawable.colors = intArrayOf(startColor, endColor)
        drawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        drawable.shape = GradientDrawable.OVAL
        drawable.orientation = GradientDrawable.Orientation.TL_BR
        setImageDrawable(drawable)
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        drawable.cornerRadius = measuredWidth / 2f
        drawable.setBounds(0, 0, measuredWidth, measuredHeight)
        setImageDrawable(drawable)
    }

    fun setColors(colors: IntArray) {
        drawable.colors = colors
    }
}