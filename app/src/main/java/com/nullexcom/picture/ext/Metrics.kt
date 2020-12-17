package com.nullexcom.editor.ext

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment

fun Context.dp(dp: Float): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

fun View.dp(dp: Float): Int {
    return context.dp(dp)
}

fun Fragment.dp(dp: Float): Int {
    return context?.dp(dp) ?: 0
}

