package com.nullexcom.picture.ui.discover

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nullexcom.picture.R

class PhotoAdapter(val context: Context) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
    private val inflater = LayoutInflater.from(context)

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(inflater.inflate(R.layout.item_photo_discover, parent, false))
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.itemView.let {

        }
    }
}