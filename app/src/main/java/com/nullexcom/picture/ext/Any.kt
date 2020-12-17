package com.nullexcom.picture.ext

fun FloatArray.copyFrom(array: FloatArray) {
    array.forEachIndexed { index, item -> this[index] = item }
}

fun Boolean.ifTrue(function: () -> Unit) {
    if (this) function.invoke()
}

fun Boolean.ifFalse(function: () -> Unit) {
    if (!this) function.invoke()
}