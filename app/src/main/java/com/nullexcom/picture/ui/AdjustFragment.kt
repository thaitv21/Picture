package com.nullexcom.picture.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nullexcom.picture.BaseEditorFragment
import com.nullexcom.picture.R
import com.yalantis.ucrop.UCrop

class AdjustFragment : BaseEditorFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_adjust, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onNewBitmap(bitmap: Bitmap) {
    }

}