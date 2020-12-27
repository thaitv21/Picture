package com.nullexcom.picture.data

import android.graphics.Point
import com.nullexcom.picture.ext.emptyMatrix
import com.nullexcom.picture.ext.matrixOf
import com.nullexcom.picture.ext.stringify
import com.nullexcom.picture.imageprocessor.*
import java.util.*
import kotlin.random.Random

class Template {

    val aspectRatio = Point()
    val modules = mutableListOf<Module>()
    private val id = (1..10).map { Random.nextInt(97, 122).toChar() }.joinToString("")

    fun getId(): String {
        return id
    }

    fun addModule(module: Module) {
        modules.add(module)
    }

    fun copyFrom(template: Template) {
        val s = template.toString()
        val clone = parse(s)
        this.modules.clear()
        this.modules.addAll(clone.modules.filter { !it.isUseless() })
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun toString(): String {
        val brightness = modules.find { it is BrightnessModule }?.let { (it as BrightnessModule).value }
                ?: 0f
        val contrast = modules.find { it is ContrastModule }?.let { (it as ContrastModule).value }
                ?: 0f
        val saturation = modules.find { it is SaturationModule }?.let { (it as SaturationModule).value }
                ?: 0f
        val colorMatrix = modules.find { it is ColorMatrixModule }?.let { (it as ColorMatrixModule).matrix }
                ?: emptyMatrix(4)
        val hsl = modules.find { it is HSLModule }?.let { (it as HSLModule).get() }
                ?: FloatArray(36)
        val blur = modules.find { it is BlurModule }?.let { (it as BlurModule).get() } ?: 0f
        return "aspect_ratio: ${aspectRatio.x}, ${aspectRatio.y}\n" +
                "brightness: ${brightness}\n" +
                "contrast: $contrast\n" +
                "saturation: $saturation \n" +
                "color_matrix: [${colorMatrix.stringify()}]\n" +
                "hsl: [${hsl.joinToString(separator = ", ")}]\n" +
                "blur: $blur"
    }

    companion object {
        fun parse(preset: String): Template {
            val template = Template()
            preset.split("\n").forEach {
                when {
                    it.startsWith("aspect_ratio") -> parseAspectRatio(template, it)
                    it.startsWith("brightness") -> parseBrightness(template, it)
                    it.startsWith("contrast") -> parseContrast(template, it)
                    it.startsWith("saturation") -> parseSaturation(template, it)
                    it.startsWith("color_matrix") -> parseColorMatrix(template, it)
                    it.startsWith("hsl") -> parseHSL(template, it)
                    it.startsWith("blur") -> parseBlur(template, it)
                }
            }
            return template
        }

        fun fromMap(map: Map<String, String>): Template {
            val template = Template()
            map.keys.forEach {
                val value = map[it] ?: ""
                when (it) {
                    "aspect_ratio" -> parseAspectRatio(template, value)
                    "brightness" -> parseBrightness(template, value)
                    "contrast" -> parseContrast(template, value)
                    "saturation" -> parseSaturation(template, value)
                    "color_matrix" -> parseColorMatrix(template, value)
                    "hsl" -> parseHSL(template, value)
                }
            }
            return template
        }

        private fun parseAspectRatio(template: Template, text: String) {
            val ratio = text.replace("aspect_ratio:", "").split(", ").map { it.trim() }
            if (ratio.size < 2) {
                template.aspectRatio.set(0, 0)
            }
            return template.aspectRatio.set(ratio[0].toInt(), ratio[1].toInt())
        }

        private fun parseBrightness(template: Template, text: String) {
            val brightness = text.replace("brightness:", "").trim().toFloat()
            template.modules.add(BrightnessModule().apply { value = brightness })
        }

        private fun parseContrast(template: Template, text: String) {
            val contrast = text.replace("contrast:", "").trim().toFloat()
            template.modules.add(ContrastModule().apply { value = contrast })
        }

        private fun parseSaturation(template: Template, text: String) {
            val saturation = text.replace("saturation:", "").trim().toFloat()
            template.modules.add(SaturationModule().apply { value = saturation })
        }

        private fun parseColorMatrix(template: Template, text: String) {
            val values = text.replace("color_matrix:", "")
                    .replace("[", "").replace("]", "")
                    .split(", ").map { it.trim().toFloat() }
            template.modules.add(ColorMatrixModule().apply { setMatrix(matrixOf(values)) })
        }

        private fun parseHSL(template: Template, text: String) {
            val values = text.replace("hsl:", "").replace("[", "")
                    .replace("]", "").split(", ")
                    .map { it.trim().toFloat() }
            template.modules.add(HSLModule().apply { set(values.toFloatArray()) })
        }

        private fun parseBlur(template: Template, text: String) {
            val blur = text.replace("blur:", "").trim().toFloat()
            template.modules.add(BlurModule().apply { set(blur) })
        }
    }
}