package com.nullexcom.picture.component

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.nullexcom.picture.ext.dp
import com.nullexcom.editor.ext.logD

class ProgressView : View {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
    }

    private val valueAnimator = ValueAnimator.ofFloat(0f, 100f).apply {
        interpolator = AccelerateInterpolator()
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.RESTART
        duration = 3000
        start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val height = if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            dp(5f)
        } else {
            measuredHeight
        }
        setMeasuredDimension(measuredWidth, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { drawProgressBar(canvas) }
    }

    private fun drawProgressBar(canvas: Canvas) {
        val progress = valueAnimator.animatedValue as Float
        logD("Progress: $progress")
        paint.strokeWidth = height.toFloat()
//        val y = height.toFloat() / 2
//        val x = width * progress / 100
//        canvas.drawLine(0f, y, x - dp(10f), y, paint)
//        canvas.drawLine(x + dp(10f), y, width.toFloat(), y, paint)
//        invalidate()
    }
}