package com.nullexcom.picture.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nullexcom.picture.data.Photo

class HistogramViewModelFactory(val photo: Photo, private val srcBitmap: Bitmap) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel =  HistogramViewModel(photo, srcBitmap)
        return viewModel as T
    }

}