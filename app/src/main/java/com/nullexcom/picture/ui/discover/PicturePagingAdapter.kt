package com.nullexcom.picture.ui.discover

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.paging.DatabasePagingOptions
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter
import com.firebase.ui.database.paging.LoadingState
import com.nullexcom.picture.R
import com.nullexcom.picture.data.Picture
import kotlinx.android.synthetic.main.item_photo_discover.view.*

class PicturePagingAdapter(val context: Context, options: DatabasePagingOptions<Picture>) : FirebaseRecyclerPagingAdapter<Picture, PicturePagingAdapter.PictureViewHolder>(options) {
    inner class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var onLoadingStateChanged: ((state: LoadingState) -> Unit)? = null

    fun setOnLoadingStateChanged(onLoadingStateChanged: (LoadingState) -> Unit) {
        this.onLoadingStateChanged = onLoadingStateChanged
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PictureViewHolder(inflater.inflate(R.layout.item_photo_discover, parent, false))
    }

    override fun onLoadingStateChanged(state: LoadingState) {
        onLoadingStateChanged?.invoke(state)
    }

    override fun onBindViewHolder(viewHolder: PictureViewHolder, position: Int, model: Picture) {
        Glide.with(context).load(model.url).into(viewHolder.itemView.imgPhoto)
        viewHolder.itemView.tvName.text = model.username
        if (!TextUtils.isEmpty(model.userPhoto)) {
            Glide.with(context).load(model.userPhoto).into(viewHolder.itemView.imgAvatar)
        }
    }
}