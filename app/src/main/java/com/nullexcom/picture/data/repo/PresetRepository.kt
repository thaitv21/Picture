package com.nullexcom.editor.data.repo

import com.nullexcom.editor.data.Preset
import javax.inject.Inject

class PresetRepository @Inject constructor(){

    fun presets() : List<Preset>{
        return emptyList()
    }
}