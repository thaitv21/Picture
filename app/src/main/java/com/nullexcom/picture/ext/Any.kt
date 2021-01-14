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

inline fun <T> T?.ifNotNull(ifFunction: (T) -> Unit) {
    if (this != null) {
        ifFunction.invoke(this)
    }
}

inline fun <T> T?.ifElseNotNull(ifFunction: (T) -> Unit, elseFunction: () -> Unit) {
    if (this != null) {
        ifFunction.invoke(this)
    } else {
        elseFunction.invoke()
    }
}