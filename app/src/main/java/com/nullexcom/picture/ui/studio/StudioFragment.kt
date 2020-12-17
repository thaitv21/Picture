package com.nullexcom.picture.ui.studio

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.nullexcom.editor.data.Photo
import com.nullexcom.picture.ext.addEndListener
import com.nullexcom.picture.*
import com.nullexcom.picture.ext.ifFalse
import com.nullexcom.picture.ui.StudioViewModel
import com.nullexcom.picture.ui.dialog.ActionDialog
import com.nullexcom.picture.ui.dialog.LoadingDialog
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_studio.*
import java.io.File
import javax.inject.Inject
import com.nullexcom.picture.ui.StudioViewModel.State

@AndroidEntryPoint
class StudioFragment : Fragment() {

    @Inject
    lateinit var viewModel: StudioViewModel
    lateinit var adapter: StudioAdapter
    private var disposable: Disposable? = null
    private lateinit var actionDialog: ActionDialog
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_studio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog()
        floatActionBar()
        recyclerViews()
        fillData()
        observer()
    }

    private fun dialog() {
        loadingDialog = LoadingDialog(context!!)
        actionDialog = ActionDialog()
    }

    private fun floatActionBar() {
        fabAddImage.setOnClickListener {
            it.rotation = 0f
            it.animate().addEndListener { addAnImage() }.setInterpolator(BounceInterpolator()).rotation(360f)
        }
    }

    private fun recyclerViews() {
        adapter = StudioAdapter()
        rvPhotos.adapter = adapter
        rvPhotos.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter.setOnItemClickListener { adjustPhoto(it) }
        adapter.setOnItemLongClickListener { showAction(it) }
    }

    private fun fillData() {
        viewModel.photos().doOnNext { adapter.submitList(it) }.subscribe()
    }

    private fun observer() {
        disposable = viewModel.state.doOnNext { render(it) }.subscribe()
    }

    private fun render(state: State) {
        when (state) {
            State.LOADING -> loadingDialog.show()
            State.NONE -> loadingDialog.cancel()
        }
    }

    override fun onDestroyView() {
        disposable?.dispose()
        super.onDestroyView()
    }

    private fun addAnImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, Actions.PICK_IMAGE)
    }

    private fun adjustPhoto(photo: Photo) {
        val ctx = context ?: return
        val dir = File(ctx.getExternalFilesDir(null), "images/cropped")
        dir.exists().ifFalse { dir.mkdir() }
        val originalUri = photo.uri
        val originalFile = originalUri.toFile()
        val croppedFile = File(dir, "${originalFile.nameWithoutExtension}_cropped.${originalFile.extension}")
        val croppedUri = Uri.fromFile(croppedFile)

        //todo aspect ratio
        UCrop.of(originalUri, croppedUri).start(ctx, this)
    }

    private fun editPhoto(uri: Uri?) {
        if (uri == null) return
        val intent = Intent(context, EditorActivity::class.java)
        val file = uri.toFile()
        intent.putExtra("name", file.nameWithoutExtension)
        intent.putExtra("uri", uri)
        startActivity(intent)
    }

    private fun showAction(photo: Photo) {
        val context = context ?: return
        actionDialog.show(parentFragmentManager, null)
        actionDialog.setOnClickPublish { viewModel.publish(context, photo) }
        actionDialog.setOnClickDelete { viewModel.delete(context, photo) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            Actions.PICK_IMAGE -> viewModel.handleAddImage(data)
            Actions.CROP_IMAGE -> data?.let { editPhoto(UCrop.getOutput(it)) }
        }
    }
}