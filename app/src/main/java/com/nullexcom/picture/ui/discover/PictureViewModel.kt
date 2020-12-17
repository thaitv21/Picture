package com.nullexcom.picture.ui.discover

import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nullexcom.picture.data.Firebase
import com.nullexcom.picture.data.Picture
import io.reactivex.rxjava3.core.Observable

class PictureViewModel : ViewModel() {

    val config = PagedList.Config.Builder().setEnablePlaceholders(false).setPrefetchDistance(2).setPageSize(20).build()

    val database: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    val query : DatabaseReference by lazy { FirebaseDatabase.getInstance().reference.child("pictures") }

    fun observers() : Observable<Picture> {
        return Firebase.observeImages()
    }
}