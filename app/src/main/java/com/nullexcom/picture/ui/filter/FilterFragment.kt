package com.nullexcom.picture.ui.filter

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nullexcom.editor.ext.logD
import com.nullexcom.picture.BaseEditorFragment
import com.nullexcom.picture.EditorViewModel
import com.nullexcom.picture.FilterViewModelFactory
import com.nullexcom.picture.R
import com.nullexcom.picture.data.Photo
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterFragment : BaseEditorFragment() {

    private val editorViewModel: EditorViewModel by lazy { editorViewModel() }
    private val photo: Photo by lazy { editorViewModel.getPhoto() }
    private val viewModel: FilterViewModel by lazy { ViewModelProvider(this, FilterViewModelFactory(photo)).get(FilterViewModel::class.java) }
    private val disposable = CompositeDisposable()
    private val adapter: FilterAdapter by lazy { FilterAdapter(requireContext(), photo) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvFilters.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvFilters.adapter = adapter
        lifecycle.addObserver(viewModel)
        adapter.onItemClickListener = { viewModel.onTemplate(it) }
        viewModel.getBitmap().doOnNext { photoView.setImageBitmap(it) }.subscribe().addTo(disposable)
        viewModel.getTemplates().doOnSuccess { adapter.submitList(it) }.subscribe().addTo(disposable)
    }

    override fun onNewBitmap(bitmap: Bitmap) {

    }

    override fun onNextAction() {
        viewModel.onNext(editorViewModel)
        if (!adapter.hasSelected()) {
            super.onNextAction()
            return
        }
        AlertDialog.Builder(context).apply {
            setTitle("Nice")
            setMessage("You have already applied this preset. Would you like adjust anything else?")
            setNegativeButton("No. Complete now!") { dialog, _ ->
                kotlin.run {
                    dialog.cancel()
                    editorViewModel.completeEditing()
                }
            }
            setPositiveButton("Yes") { dialog, _ ->
                kotlin.run {
                    dialog.cancel()
                    super.onNextAction()
                }
            }
        }.show()
    }

    override fun onDestroyView() {
        disposable.dispose()
        super.onDestroyView()
    }
}