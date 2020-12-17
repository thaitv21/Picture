package com.nullexcom.picture

import android.content.Context
import androidx.annotation.StringRes

object Strings {

    lateinit var context: Context

    fun loadResources(context: Context) {
        this.context = context
    }

    fun get(@StringRes id: Int) : String {
        return context.getString(id)
    }
}