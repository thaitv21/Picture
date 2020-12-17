package com.nullexcom.picture.ui.blur

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nullexcom.picture.BaseEditorFragment
import com.nullexcom.picture.R
import com.nullexcom.picture.component.MiddleProgressBar
import com.nullexcom.picture.component.StartProgressBar
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_blur.*

class BlurFragment : BaseEditorFragment() {

    private val viewModel: BlurViewModel by lazy { BlurViewModel(editorViewModel().getFilteredBitmap(), editorViewModel().template.modules) }
    private lateinit var disposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_blur, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposable = viewModel.getBlurredBitmap().doOnNext { photoView.setImageBitmap(it) }.subscribe()
        pbBlur.setOnProgressChangedListener { progress, fromUser ->
            if (fromUser) {
                handleProgressChanged(progress)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
    }

    private fun handleProgressChanged(progress: Int) {
        tvValue.text = progress.toString()
        viewModel.onRadiusChanged(progress / 10f)
    }

    override fun onNextAction() {
        super.onNextAction()

    }

    override fun onNewBitmap(bitmap: Bitmap) {
        photoView.setImageBitmap(bitmap)
    }
}