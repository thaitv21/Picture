package com.nullexcom.picture.ext

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.nullexcom.picture.ApplicationContextCompat

fun Context.dp(dp: Float): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

fun View.dp(dp: Float): Int {
    return context.dp(dp)
}

fun Fragment.dp(dp: Float): Int {
    return context?.dp(dp) ?: 0
}

fun dp(dp: Float): Int {
    return ApplicationContextCompat.getInstance().dp(dp)
}