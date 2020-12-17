package com.nullexcom.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nullexcom.picture.HistogramFragment.Companion.BRIGHTNESS
import com.nullexcom.picture.HistogramFragment.Companion.COLOR_MATRIX
import com.nullexcom.picture.HistogramFragment.Companion.CONTRAST
import com.nullexcom.picture.HistogramFragment.Companion.HSL
import com.nullexcom.picture.HistogramFragment.Companion.SATURATION
import kotlinx.android.synthetic.main.page_histogram.*

class PageHistogramFragment : Fragment() {

    private var onPageSelected: ((Int) -> Unit)? = null

    private lateinit var items : List<MenuAdapter.Item>

    fun setOnPageSelected(onPageSelected: (Int) -> Unit) {
        this.onPageSelected = onPageSelected
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_histogram, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        items = listOf(
                MenuAdapter.Item(BRIGHTNESS, getString(R.string.label_brightness), R.drawable.ic_sun),
                MenuAdapter.Item(CONTRAST, getString(R.string.label_contrast), R.drawable.ic_contrast),
                MenuAdapter.Item(SATURATION, getString(R.string.label_saturation), R.drawable.ic_saturation),
                MenuAdapter.Item(COLOR_MATRIX, getString(R.string.label_color_matrix), R.drawable.ic_matrix),
                MenuAdapter.Item(HSL, getString(R.string.label_hsv), R.drawable.ic_logo)
        )
        rvMenu.adapter = MenuAdapter(context!!, items).apply { setOnItemClickListener { handleItem(it) } }
        rvMenu.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun handleItem(position: Int) {
        val item = items[position]
        onPageSelected?.invoke(item.id)
    }
}