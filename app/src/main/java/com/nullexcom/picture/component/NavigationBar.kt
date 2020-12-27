package com.nullexcom.editor.component

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import com.nullexcom.picture.R
import com.nullexcom.picture.ext.dp

class NavigationBar : FrameLayout {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    init {
        val iconBack = buildIcon()
        addView(iconBack)
    }

    private fun buildIcon() : ImageView {
        val imageView = ImageView(context)
        val params = LayoutParams(dp(45f), dp(45f))
        params.gravity = Gravity.CENTER_VERTICAL
        imageView.layoutParams = params
        imageView.setPadding(dp(10f), dp(10f), dp(10f), dp(10f))
        imageView.setImageResource(R.drawable.ic_arrow_back)
        imageView.imageTintList = ColorStateList.valueOf(Color.BLACK)
        return imageView
    }

}