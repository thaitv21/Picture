package com.nullexcom.picture.ext

import android.graphics.Color
import kotlin.math.abs

const val RED: Int = 0xfffe2712.toInt()
const val RED_ORANGE: Int = 0xffff3f34.toInt()
const val ORANGE: Int = 0xfffb9902.toInt()
const val YELLOW_ORANGE = 0xfffabc02.toInt()
const val YELLOW = 0xfffefe33.toInt()
const val YELLOW_GREEN = 0xffd0ea2b.toInt()
const val GREEN = 0xff66b032.toInt()
const val BLUE_GREEN = 0xff0391ce.toInt()
const val BLUE = 0xff0247fe.toInt()
const val BLUE_VIOLET = 0xff3d01a4.toInt()
const val VIOLET = 0xff8601af.toInt()
const val RED_VIOLET = 0xffa7194b.toInt()

fun String.toColor(): Int {
    val hex = if (this.startsWith("#")) this else "#${this}"
    return Color.parseColor(hex)
}

fun Int.toRGB(): IntArray {
    val r = Color.red(this);
    val g = Color.green(this)
    val b = Color.blue(this)
    return intArrayOf(r, g, b)
}

fun Int.toHSL() : FloatArray {
    val r = Color.red(this) / 255f
    val g = Color.green(this) / 255f
    val b = Color.blue(this) / 255f
    val cmin: Float = minOf(r, g, b)
    val cmax: Float = maxOf(r, g, b)
    val c = cmax - cmin
    val l = (cmax + cmin) / 2
    var s = 0f
    if (c != 0f) {
        s = c / (1 - abs(2 * l - 1))
    }
    var h = 0f
    if (c == 0f) {
        h = 0f
    } else if (cmax == r) {
        h = 60 * ((g - b) / c)
    } else if (cmax == g) {
        h = 60 * (2 + (b - r) / c)
    } else if (cmax == b) {
        h = 60 * (4 + (r - g) / c)
    }
    return floatArrayOf(h, s, l)
}