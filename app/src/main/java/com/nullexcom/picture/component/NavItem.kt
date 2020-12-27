package com.nullexcom.editor.component

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.nullexcom.picture.R
import com.nullexcom.picture.ext.dp

class NavItem : LinearLayout {
    constructor(context: Context) : super(context) {init(null)}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {init(attrs)}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {init(attrs)}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {init(attrs)}

    private lateinit var iconView: ImageView
    private lateinit var labelView: TextView
    private var icon: Drawable? = null
    private var label: String? = null

    private fun init(attrs: AttributeSet?) {
        parseAttrs(attrs)
        iconView = buildIcon()
        labelView = buildLabel()
        addView(iconView)
        addView(labelView)
        gravity = Gravity.CENTER
        orientation = VERTICAL
    }

    private fun parseAttrs(attributeSet: AttributeSet?) {
        val attrs = attributeSet ?: return
        val ta = context.obtainStyledAttributes(attrs, R.styleable.NavItem)
        icon = ta.getDrawable(R.styleable.NavItem_android_icon)
        label = ta.getString(R.styleable.NavItem_android_label)
        ta.recycle()
    }

    private fun buildIcon() : ImageView {
        val iconView = ImageView(context)
        val params = LayoutParams(dp(20f), dp(20f))
        params.gravity = Gravity.CENTER
        iconView.setImageDrawable(icon)
        iconView.layoutParams = params
        iconView.imageTintList = ColorStateList.valueOf(Color.BLACK)
        return iconView;
    }

    private fun buildLabel() : TextView {
        val labelView = TextView(context)
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        params.topMargin = dp(5f)
        labelView.setTextColor(Color.BLACK)
        labelView.layoutParams = params
        labelView.text = label
        return labelView
    }
}