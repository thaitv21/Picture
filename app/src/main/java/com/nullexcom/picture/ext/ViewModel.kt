package com.nullexcom.picture.ext

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.nullexcom.picture.AppState
import com.nullexcom.picture.Strings

fun ViewModel.getString(@StringRes id: Int) : String {
    return Strings.get(id)
}