package com.nullexcom.picture

import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.nullexcom.editor.ext.doOnCompleted
import com.nullexcom.editor.ext.logD
import com.nullexcom.picture.ext.alert
import com.nullexcom.picture.ui.EditorState
import com.nullexcom.picture.ui.blur.BlurFragment
import com.nullexcom.picture.ui.dialog.CompletedDialog
import com.nullexcom.picture.ui.dialog.LoadingDialog
import com.nullexcom.picture.ui.dialog.ProgressDialog
import com.nullexcom.picture.ui.dialog.SaveDialog
import com.nullexcom.picture.ui.edit.SetWallpaperReceiver
import com.nullexcom.picture.ui.filter.FilterFragment
import com.nullexcom.picture.ui.histogram.HistogramFragment
import com.nullexcom.picture.ui.more.MoreFragment
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_editor.*

class EditorActivity : AppCompatActivity() {

    lateinit var viewModel: EditorViewModel
    private lateinit var disposable: Disposable
    private var backStackCount = 0

    private lateinit var progressDialog: ProgressDialog
    private lateinit var completedDialog: CompletedDialog
    private val loadingDialog by lazy { LoadingDialog(this) }
    private val receiver = SetWallpaperReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        viewModel = EditorViewModel(intent)
        disposable = viewModel.getPageState().doOnNext { handlePage(it) }.subscribe()
        tvNext.setOnClickListener { onNextClick() }
        btnDone.setOnClickListener { clickDone() }
        Glide.with(this).asBitmap().load(intent.getParcelableExtra("uri")!!).doOnCompleted {
            logD(it.config.toString())
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                finish()
                return@addOnBackStackChangedListener
            }
            if (supportFragmentManager.backStackEntryCount < backStackCount) {
                supportFragmentManager.fragments.find { it.isVisible && it is BaseEditorFragment }?.let {
                    (it as BaseEditorFragment).onBackAction()
                }
                viewModel.onBack()
            }
            backStackCount = supportFragmentManager.backStackEntryCount
        }
        progressDialog = ProgressDialog()
        completedDialog = CompletedDialog().apply {
            setOnClickSetWallpaper { viewModel.setWallpaper(this@EditorActivity) }
            setOnClickPublish { viewModel.publishImage() }
            setOnClickShare { viewModel.share() }
            setOnClickCancel { viewModel.finish() }
        }
        viewModel.isLoading.doOnNext { showLoading(it) }.subscribe()
        registerReceiver(receiver, IntentFilter(Intent.ACTION_WALLPAPER_CHANGED))
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            loadingDialog.show()
        } else {
            loadingDialog.cancel()
        }
    }

    private fun onNextClick() {
        val fragment = supportFragmentManager.fragments.find { it.isVisible && it is BaseEditorFragment }?.let { it as BaseEditorFragment }
        fragment?.onNextAction()
    }

    private fun handlePage(state: EditorState) {
        if (!state.shouldCreate) return
        when (state) {
            EditorState.Error -> error()
            EditorState.ShouldFinish -> finish()
            EditorState.AskSave -> askSaving()
            EditorState.Saving -> showSaving()
            EditorState.Completed -> complete()
            EditorState.Done -> finish()
            else -> showFragment(state)
        }
    }

    private fun askSaving() {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            return
        }
        val dialog = SaveDialog()
        dialog.setOnSaveClick {
            viewModel.saveImage(it)
            dialog.dismiss()
        }
        dialog.setOnCancel { viewModel.onCancelSaving() }
        dialog.show(supportFragmentManager, "")
    }

    private fun showSaving() {
        progressDialog.show(supportFragmentManager, "")
    }

    private fun complete() {
        progressDialog.dismiss()
        completedDialog.isCancelable = false
        completedDialog.show(supportFragmentManager, "")
    }

    private fun showFragment(state: EditorState) {
        val fragment = when (state) {
            EditorState.Filter -> FilterFragment()
            EditorState.Histogram -> HistogramFragment()
            EditorState.Blur -> BlurFragment()
            EditorState.More -> MoreFragment()
            else -> null
        }
        fragment?.let { showFragment(it) }
        if (state is EditorState.Filter) {
            btnDone.visibility = View.GONE
        } else {
            btnDone.visibility = View.VISIBLE
        }
    }

    private fun showFragment(fragment: BaseEditorFragment) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.apply {
            if (fm.backStackEntryCount == 0) {
                add(R.id.container, fragment)
            } else {
                replace(R.id.container, fragment)
            }
            addToBackStack(null)
            commit()
        }
    }

    override fun onDestroy() {
        disposable.dispose()
        unregisterReceiver(receiver)
        super.onDestroy()
    }


    private fun error() {
        alert("Oops!", getString(R.string.error_editor))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                askSaving()
            } else {
                alert("Oops", getString(R.string.error_permission)) {
                    askSaving()
                }
            }
        }
    }

    private fun clickDone() {
        AlertDialog.Builder(this).apply {
            setTitle("Hmm")
            setMessage("We will skip the next editing steps. You definitely want to do that, right?")
            setNegativeButton("No") { dialog, _ -> dialog.cancel() }
            setPositiveButton("Yes") { dialog, _ ->
                kotlin.run {
                    dialog.cancel()
                    viewModel.completeEditing()
                }
            }
        }.show()
    }
}