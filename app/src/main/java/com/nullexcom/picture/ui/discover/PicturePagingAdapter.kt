package com.nullexcom.picture.ui.discover

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.firebase.ui.database.paging.DatabasePagingOptions
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter
import com.firebase.ui.database.paging.LoadingState
import com.nullexcom.editor.ext.OnItemClickListener
import com.nullexcom.picture.R
import com.nullexcom.picture.data.Picture
import kotlinx.android.synthetic.main.item_photo_discover.view.*

class PicturePagingAdapter(val context: Context, options: DatabasePagingOptions<Picture>) : FirebaseRecyclerPagingAdapter<Picture, PicturePagingAdapter.PictureViewHolder>(options) {
    inner class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var onLoadingStateChanged: ((state: LoadingState) -> Unit)? = null
    private var onItemClickListener: OnItemClickListener<Picture>? = null

    fun setOnLoadingStateChanged(onLoadingStateChanged: (LoadingState) -> Unit) {
        this.onLoadingStateChanged = onLoadingStateChanged
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<Picture>?) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PictureViewHolder(inflater.inflate(R.layout.item_photo_discover, parent, false))
    }

    override fun onLoadingStateChanged(state: LoadingState) {
        onLoadingStateChanged?.invoke(state)
    }

    override fun onBindViewHolder(viewHolder: PictureViewHolder, position: Int, model: Picture) {
        viewHolder.itemView.tvName.text = model.username
        if (!TextUtils.isEmpty(model.userPhoto)) {
            Glide.with(context).load(model.userPhoto).into(viewHolder.itemView.imgAvatar)
        }
        Glide.with(context).load(model.url).into(object : DrawableImageViewTarget(viewHolder.itemView.imgPhoto) {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                super.onResourceReady(resource, transition)
                viewHolder.itemView.loadingView.visibility = View.GONE
            }
        })
        viewHolder.itemView.setOnClickListener { onItemClickListener?.invoke(model) }
    }
}