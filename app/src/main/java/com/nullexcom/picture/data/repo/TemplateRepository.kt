package com.nullexcom.picture.data.repo

import com.nullexcom.picture.ApplicationContextCompat
import com.nullexcom.picture.data.SampleTemplate
import com.nullexcom.picture.data.Template
import com.nullexcom.picture.imageprocessor.LUTModule

class TemplateRepository {

    fun getTemplates(): List<Template> {
        val list = mutableListOf<Template>()
        list.addAll(defaultTemplates())
        list.addAll(lutTemplates())
        return list
    }

    private fun defaultTemplates(): List<Template> {
        return listOf(
                SampleTemplate.Default,
                SampleTemplate.BlackAndWhite,
                SampleTemplate.Sepium,
                SampleTemplate.Purple,
                SampleTemplate.Yellow
        )
    }

    private fun lutTemplates(): List<Template> {
        val context = ApplicationContextCompat.getInstance().getApplicationContext()
        val files = context.assets.list("luts") ?: return emptyList()
        return files.map { Template().apply { addModule(LUTModule(it)) } }
    }
}