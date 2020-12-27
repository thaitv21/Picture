package com.nullexcom.picture.ui.histogram

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.nullexcom.picture.*
import com.nullexcom.picture.ext.dp
import com.nullexcom.picture.viewmodels.HistogramViewModel
import com.nullexcom.picture.viewmodels.HistogramViewModelFactory
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.android.synthetic.main.fragment_histogram.*

class HistogramFragment : BaseEditorFragment() {

    private val editorViewModel: EditorViewModel by lazy { editorViewModel() }

    private val viewModel by viewModels<HistogramViewModel> { HistogramViewModelFactory(editorViewModel.getPhoto(), editorViewModel.getCurrentBitmap()) }

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
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_histogram, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.adapter = PagerAdapter(this)
        viewPager.isUserInputEnabled = false
        viewPager.registerOnPageChangeCallback(onPageChangedCallback)
        imgClose.setOnClickListener { selectPage(0) }
        lifecycle.addObserver(viewModel)
        viewModel.getBitmap().doOnNext { photoView.setImageBitmap(it) }.subscribe().addTo(compositeDisposable)
    }

    override fun onDestroyView() {
        viewPager.unregisterOnPageChangeCallback(onPageChangedCallback)
        compositeDisposable.dispose()
        super.onDestroyView()
    }

    private fun selectPage(index: Int) {
        viewPager.setCurrentItem(index, false)
        val translate = if (index == 0) dp(30f) else 0
        imgClose.animate().translationY(translate.toFloat()).start()
    }

    override fun onNextAction() {
        super.onNextAction()
        viewModel.onNext(editorViewModel)
    }

    override fun onBackAction() {
        super.onBackAction()
        viewModel.onBack(editorViewModel)
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

    inner class ViewPager2PageChangeCallback : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val fragment = pages[position]
            updateViewPagerHeight(dp(if (fragment is PageColorMatrixFragment) 150f else 100f))
            if (fragment is OnTemplateChangedListener) {
                (fragment as OnTemplateChangedListener).onChanged(viewModel.getTemplate())
            }
        }
    }
}