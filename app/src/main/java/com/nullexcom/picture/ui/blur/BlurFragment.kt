package com.nullexcom.picture.ui.blur

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nullexcom.picture.BaseEditorFragment
import com.nullexcom.picture.EditorViewModel
import com.nullexcom.picture.R
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.android.synthetic.main.fragment_blur.*

class BlurFragment : BaseEditorFragment() {

    private val editorViewModel: EditorViewModel by lazy { editorViewModel() }
    private val viewModel: BlurViewModel by lazy { BlurViewModel(editorViewModel.getPhoto(), editorViewModel.getCurrentBitmap()) }
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_blur, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)
        viewModel.getBlurredBitmap().doOnNext { photoView.setImageBitmap(it) }.subscribe().addTo(compositeDisposable)
        pbBlur.setOnProgressChangedListener { progress, fromUser ->
            if (fromUser) {
                handleProgressChanged(progress)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
    }

    private fun handleProgressChanged(progress: Int) {
        tvValue.text = progress.toString()
        viewModel.onRadiusChanged(progress / 10f)
    }

    override fun onBackAction() {
        super.onBackAction()
        viewModel.onBack(editorViewModel)
    }

    override fun onNewBitmap(bitmap: Bitmap) {
        photoView.setImageBitmap(bitmap)
    }
}