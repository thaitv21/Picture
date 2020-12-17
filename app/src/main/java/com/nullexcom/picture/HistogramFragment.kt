package com.nullexcom.picture

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.renderscript.RenderScript
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.nullexcom.editor.ext.dp
import com.nullexcom.picture.data.BrightnessModule
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_histogram.*

class HistogramFragment : BaseEditorFragment() {

    private val viewModel by lazy { HistogramViewModel(editorViewModel().getCropped(), editorViewModel().template.modules) }

    companion object {
        const val BRIGHTNESS = 1
        const val CONTRAST = 2
        const val SATURATION = 3
        const val COLOR_MATRIX = 4
        const val HSL = 5
    }

    private val onViewPageSelected: ((Int) -> Unit) = { id ->
        when (id) {
            BRIGHTNESS -> selectPage(1)
            CONTRAST -> selectPage(2)
            SATURATION -> selectPage(3)
            COLOR_MATRIX -> selectPage(4)
            HSL -> selectPage(5)
        }
    }

    private val pages = listOf(
            PageHistogramFragment().apply { setOnPageSelected(onViewPageSelected) },
            PageBrightnessFragment().apply { setOnValueChanged { viewModel.onBrightnessChanged(it) } },
            PageContrastFragment().apply { setOnValueChanged { viewModel.onContrastChanged(it) } },
            PageSaturationFragment().apply { setOnValueChanged { viewModel.onSaturationChanged(it) } },
            PageColorMatrixFragment().apply { setOnMatrixChanged { viewModel.onColorMatrixChanged(it) } },
            PageHSLFragment().apply { setOnValuesChanged { viewModel.onHSLChanged(it) } }
    )

    private val onPageChangedCallback = ViewPager2PageChangeCallback()
    private var disposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_histogram, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.adapter = PagerAdapter(this)
        viewPager.isUserInputEnabled = false
        viewPager.registerOnPageChangeCallback(onPageChangedCallback)
        imgClose.setOnClickListener { selectPage(0) }
        disposable = viewModel.filteredBitmap.doOnNext { photoView.setImageBitmap(it) }.subscribe()
    }

    override fun onResume() {
        super.onResume()
//        viewModel.requestFilteredBitmap()
    }

    override fun onDestroyView() {
        viewPager.unregisterOnPageChangeCallback(onPageChangedCallback)
        disposable?.dispose()
        super.onDestroyView()
    }

    private fun selectPage(index: Int) {
        viewPager.setCurrentItem(index, false)
        val translate = if (index == 0) dp(30f) else 0
        imgClose.animate().translationY(translate.toFloat()).start()
    }

    override fun onNextAction() {
        super.onNextAction()
        editorViewModel().setFilteredBitmap(viewModel.filteredBitmap.value)
    }

    override fun onNewBitmap(bitmap: Bitmap) {
    }

    inner class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = pages.size

        override fun createFragment(position: Int): Fragment = pages[position]
    }

    fun updateViewPagerHeight(h: Int) {
        val layoutParams = viewPager.layoutParams.apply {
            this.height = h
        }
        viewPager.layoutParams = layoutParams
    }

    inner class ViewPager2PageChangeCallback() : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val fragment = pages[position]
            if (fragment is PageColorMatrixFragment) {
                updateViewPagerHeight(dp(150f))
                return
            }
            updateViewPagerHeight(dp(100f))
        }
    }
}