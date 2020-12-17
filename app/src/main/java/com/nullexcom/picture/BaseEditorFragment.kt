package com.nullexcom.picture

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseEditorFragment : Fragment() {

    private lateinit var disposable: Disposable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = editorViewModel() ?: return
        val appState = viewModel.appState
        disposable = appState.getOriginalBitmap().doOnNext { onNewBitmap(it) }.subscribe()
    }

    override fun onDestroyView() {
        disposable.dispose()
        super.onDestroyView()
    }

    fun editorViewModel(): EditorViewModel {
        return requireActivity().let { (it as EditorActivity).viewModel }
    }

    abstract fun onNewBitmap(bitmap: Bitmap)

    open fun onNextAction() {

    }
}