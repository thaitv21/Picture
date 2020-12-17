package com.nullexcom.picture

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nullexcom.picture.component.MiddleProgressBar
import com.nullexcom.picture.ext.Matrix
import com.nullexcom.picture.ext.emptyMatrix
import com.nullexcom.picture.ext.matrixOf
import kotlinx.android.synthetic.main.page_color_matrix.*

class PageColorMatrixFragment : Fragment() {

    private lateinit var colorItems: List<ColorItem>
    private var currentIndex = 0
    private var onMatrixChanged: ((Matrix) -> Unit)? = null
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
}