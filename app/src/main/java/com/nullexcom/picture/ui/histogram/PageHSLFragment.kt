package com.nullexcom.picture.ui.histogram

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nullexcom.picture.R
import com.nullexcom.picture.component.MiddleProgressBar
import com.nullexcom.picture.ext.*
import com.nullexcom.picture.imageprocessor.HSLModule
import com.nullexcom.picture.viewmodels.HistogramViewModel
import kotlinx.android.synthetic.main.item_color.view.*
import kotlinx.android.synthetic.main.page_hsl.*
import kotlin.math.roundToInt

class PageHSLFragment : Fragment() {

    private var currentIndex = 0
    private val gradientColors = listOf(
            intArrayOf("C41432".toColor(), "FC5A44".toColor()), //red
            intArrayOf("E30904".toColor(), "FC8044".toColor()), //red-orange
            intArrayOf("F0650A".toColor(), "F49923".toColor()), //orange
            intArrayOf("F49923".toColor(), "FAE045".toColor()), //orange-yellow
            intArrayOf("FAE045".toColor(), "FAD961".toColor()), //yellow
            intArrayOf("FAD961".toColor(), "D5FF00".toColor()), //y-g
            intArrayOf("4DD750".toColor(), "C0FF8B".toColor()), //g
            intArrayOf("ADEF38".toColor(), "167FFF".toColor()), //g-b
            intArrayOf("085FE5".toColor(), "3ED0FE".toColor()), //b
            intArrayOf("6A24AA".toColor(), "20CEFA".toColor()), //b-v
            intArrayOf("5A009F".toColor(), "D252FF".toColor()), //v
            intArrayOf("FA69B9".toColor(), "6A45D4".toColor()) //v-r
    )
    private val values = FloatArray(36) { 0f }
    private var onValuesChanged: ((FloatArray) -> Unit)? = null
    private val viewModel: HistogramViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_hsl, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ColorAdapter(view.context)
        rvColors.adapter = adapter
        rvColors.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        pbHSLValue.setOnProgressChangedListener(MiddleProgressBar.OnProgressChangedListener { progress, fromUser ->
            if (fromUser) {
                handleValueChanged(progress)
            }
        })
        fillData()
    }

    private fun fillData() {
        val template = viewModel.getTemplate()
        val module = template.modules.find { it is HSLModule } ?: return
        val hslModule = module as HSLModule
        if (hslModule.isUseless()) return
        val hslValues = hslModule.get()
        hslValues.forEachIndexed { index, value -> values[index] = value }
        val value = values[3 * currentIndex] * 100f / 30f
        pbHSLValue.setValue(value.roundToInt())
    }

    fun setOnValuesChanged(onValuesChanged: ((FloatArray) -> Unit)) {
        this.onValuesChanged = onValuesChanged
    }

    private fun handleValueChanged(progress: Int) {
        values[3 * currentIndex] = 30 * progress / 100f
        onValuesChanged?.invoke(values)
    }

    inner class ColorAdapter(val context: Context) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

        private val inflater = LayoutInflater.from(context)

        inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
            return ColorViewHolder(inflater.inflate(R.layout.item_color, parent, false))
        }

        override fun getItemCount(): Int = gradientColors.size

        override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
            holder.itemView.imgColor.setColors(gradientColors[position])
            holder.itemView.setOnClickListener { handleItemClick(position) }
            holder.itemView.imgSelected.visibility = if (currentIndex == position) View.VISIBLE else View.GONE;
        }

        private fun handleItemClick(position: Int) {
            val oldPos = currentIndex
            currentIndex = position
            notifyItemChanged(oldPos)
            notifyItemChanged(currentIndex)
        }
    }
}