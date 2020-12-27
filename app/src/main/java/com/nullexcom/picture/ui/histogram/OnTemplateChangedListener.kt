package com.nullexcom.picture.ui.histogram

import com.nullexcom.picture.data.Template

interface OnTemplateChangedListener {
    fun onChanged(template: Template)
}