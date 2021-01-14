package com.nullexcom.picture.ui.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nullexcom.picture.R
import kotlinx.android.synthetic.main.dialog_save.*

class SaveDialog : BottomSheetDialogFragment() {

    private var onSaveClick: ((String) -> Unit)? = null
    private var onCancel: (() -> Unit)? = null
    private var listenCancel = true

    fun setOnSaveClick(onSaveClick: (String) -> Unit) {
        this.onSaveClick = onSaveClick
    }

    fun setOnCancel(onCancel: () -> Unit) {
        this.onCancel = onCancel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_save, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filename = "Picture_${System.currentTimeMillis()}.png"
        edtFilename.setText(filename)
        btnSave.setOnClickListener {
            listenCancel = false
            onSaveClick?.invoke(edtFilename.text.toString().trim())
        }
    }

    override fun onStart() {
        super.onStart()
        (requireView().parent as View).setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (listenCancel) {
            onCancel?.invoke()
        }
    }
}