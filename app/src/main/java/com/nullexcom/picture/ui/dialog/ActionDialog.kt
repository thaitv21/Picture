package com.nullexcom.picture.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nullexcom.picture.R
import kotlinx.android.synthetic.main.dialog_action.*
import kotlinx.android.synthetic.main.dialog_completed.*
import kotlinx.android.synthetic.main.dialog_completed.viewPublish
import kotlinx.android.synthetic.main.dialog_completed.viewSetWallpaper
import kotlinx.android.synthetic.main.dialog_completed.viewShare

class ActionDialog: BottomSheetDialogFragment() {

    private var onClickSetWallpaper: (() -> Unit)? = null
    private var onClickPublish: (() -> Unit)? = null
    private var onClickShare: (() -> Unit)? = null
    private var onClickDelete: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_action, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewSetWallpaper.setOnClickListener { handleClick(onClickSetWallpaper) }
        viewPublish.setOnClickListener { handleClick(onClickPublish) }
        viewShare.setOnClickListener { handleClick(onClickShare) }
        viewDelete.setOnClickListener { handleClick(onClickDelete) }
    }

    private fun handleClick(onClick: (() -> Unit)?) {
        onClick?.invoke()
        dismiss()
    }

    fun setOnClickSetWallpaper(onClickSetWallpaper: () -> Unit) {
        this.onClickSetWallpaper = onClickSetWallpaper
    }

    fun setOnClickPublish(onClickPublish: () -> Unit) {
        this.onClickPublish = onClickPublish
    }

    fun setOnClickShare(onClickShare: () -> Unit) {
        this.onClickShare = onClickShare
    }

    fun setOnClickDelete(onClickDelete: () -> Unit) {
        this.onClickDelete = onClickDelete
    }
}