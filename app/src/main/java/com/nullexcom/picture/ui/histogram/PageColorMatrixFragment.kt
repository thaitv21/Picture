package com.nullexcom.picture.ui.histogram

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nullexcom.picture.ColorAdapter
import com.nullexcom.picture.R
import com.nullexcom.picture.ext.Matrix
import com.nullexcom.picture.ext.matrixOf
import com.nullexcom.picture.imageprocessor.ColorMatrixModule
import com.nullexcom.picture.viewmodels.HistogramViewModel
import kotlinx.android.synthetic.main.page_color_matrix.*
import kotlin.math.roundToInt

class PageColorMatrixFragment : Fragment() {

    private lateinit var colorItems: List<ColorItem>
    private var currentIndex = 0
    private var onMatrixChanged: ((Matrix) -> Unit)? = null
    private val viewModel: HistogramViewModel by viewModels({ requireParentFragment() })
    private var matrix = matrixOf(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
    )

    fun setOnMatrixChanged(onMatrixChanged: ((Matrix) -> Unit)?) {
        this.onMatrixChanged = onMatrixChanged
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_color_matrix, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        colorItems = listOf("RED", "GREEN", "BLUE").map { ColorItem(it) }
        val adapter = ColorAdapter(view.context)
        adapter.setOnItemSelected { onItemSelected(it) }
        rvColors.adapter = adapter
        rvColors.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        pbValueOne.setOnProgressChangedListener { progress, fromUser -> if (fromUser) onValueOneChanged(progress) }
        pbValueTwo.setOnProgressChangedListener { progress, fromUser -> if (fromUser) onValueTwoChanged(progress) }
    }

    private fun onItemSelected(index: Int) {
        currentIndex = index
        val colorItem = colorItems[index]
        pbValueOne.setValue(colorItem.valueOne)
        pbValueTwo.setValue(colorItem.valueTwo)
        tvLabelOne.text = colorItem.labelOne
        tvLabelTwo.text = colorItem.labelTwo
        tvValueOne.text = colorItem.valueOne.toString()
        tvValueTwo.text = colorItem.valueTwo.toString()
    }

    private fun onValueOneChanged(value: Int) {
        val colorItem = colorItems[currentIndex]
        colorItem.valueOne = value
        tvValueOne.text = value.toString()
        onMatrixChanged()
    }

    private fun onValueTwoChanged(value: Int) {
        val colorItem = colorItems[currentIndex]
        colorItem.valueTwo = value
        tvValueTwo.text = value.toString()
        onMatrixChanged()
    }

    private fun onMatrixChanged() {
        matrix[1][0] = colorItems[0].toValueOne()
        matrix[2][0] = colorItems[0].toValueTwo()
        matrix[0][1] = colorItems[1].toValueOne()
        matrix[2][1] = colorItems[1].toValueTwo()
        matrix[0][2] = colorItems[2].toValueOne()
        matrix[1][2] = colorItems[2].toValueTwo()
        onMatrixChanged?.invoke(matrix)
    }

    inner class ColorItem(val color: String) {
        var valueOne: Int = 0
        var valueTwo: Int = 0
        val labelOne: String
        val labelTwo: String

        init {
            when (color) {
                "RED" -> {
                    labelOne = getString(R.string.green)
                    labelTwo = getString(R.string.blue)
                }
                "GREEN" -> {
                    labelOne = getString(R.string.red)
                    labelTwo = getString(R.string.blue)
                }
                else -> {
                    labelOne = getString(R.string.red)
                    labelTwo = getString(R.string.green)
                }
            }
        }

        fun toValueOne(): Float {
            return 2 * valueOne / 100f
        }

        fun toValueTwo(): Float {
            return 2 * valueTwo / 100f
        }
    }

    override fun onResume() {
        super.onResume()
        val template = viewModel.getTemplate()
        val colorMatrixModule = template.modules.find { it is ColorMatrixModule } ?: return
        val colorMatrix = (colorMatrixModule as ColorMatrixModule).matrix
        colorItems[0].valueOne = calculateProgress(colorMatrix[0][1])
        colorItems[0].valueTwo = calculateProgress(colorMatrix[0][2])
        colorItems[1].valueOne = calculateProgress(colorMatrix[1][0])
        colorItems[1].valueTwo = calculateProgress(colorMatrix[1][2])
        colorItems[2].valueOne = calculateProgress(colorMatrix[2][0])
        colorItems[2].valueTwo = calculateProgress(colorMatrix[2][1])
        onItemSelected(currentIndex)

    }

    private fun calculateProgress(value: Float) : Int {
        return (50f * value).roundToInt()
    }
}