package com.nullexcom.picture.component

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.nullexcom.editor.ext.dp
import com.nullexcom.editor.ext.newImageView
import com.nullexcom.editor.ext.newTextView
import com.nullexcom.picture.R

class TabBarItem : LinearLayout {
    private val isActive = false
    private lateinit var icon: ImageView
    private lateinit var label: TextView

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    private fun init(context: Context) {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        icon = buildIcon(context)
        label = buildLabel(context)
        addView(icon)
        addView(label)
    }

    private fun buildIcon(context: Context): ImageView {
        val imageView = context.newImageView()
        val layoutParams = LayoutParams(dp(18f), dp(18f))
        imageView.layoutParams = layoutParams
        imageView.imageTintList = ColorStateList.valueOf(Color.BLACK)
        return imageView
    }

    private fun buildLabel(context: Context) : TextView {
        val textView = context.newTextView()
        textView.setTextColor(Color.BLACK)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        textView.typeface = ResourcesCompat.getFont(context, R.font.lato_bold)
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.topMargin = dp(5f)
        textView.layoutParams = layoutParams
        return textView
    }

    fun setIcon(drawable: Drawable) {
        icon.setImageDrawable(drawable)
    }

    fun setLabel(text: String) {
        label.text = text
    }

    fun setActive(isActive: Boolean) {
        val color = if (isActive) context.getColor(R.color.tabActive) else context.getColor(R.color.tabInactive)
        icon.imageTintList = ColorStateList.valueOf(color)
        label.setTextColor(color)
    }
}