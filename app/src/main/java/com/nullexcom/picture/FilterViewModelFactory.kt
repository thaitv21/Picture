package com.nullexcom.picture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nullexcom.picture.data.Photo
import com.nullexcom.picture.ui.filter.FilterViewModel

class FilterViewModelFactory(val photo: Photo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FilterViewModel(photo) as T
    }
}