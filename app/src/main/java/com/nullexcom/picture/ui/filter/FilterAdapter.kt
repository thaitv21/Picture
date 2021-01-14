package com.nullexcom.picture.ui.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nullexcom.editor.ext.OnItemClickListener
import com.nullexcom.picture.imageprocessor.ImageProcessor
import com.nullexcom.picture.R
import com.nullexcom.picture.data.Photo
import com.nullexcom.picture.data.Template
import com.nullexcom.picture.ext.bitmapSize
import com.nullexcom.picture.ext.dp
import com.nullexcom.picture.ext.scaleToFitHeight
import kotlinx.android.synthetic.main.item_filter.view.*

class FilterAdapter(val context: Context, val photo: Photo) : ListAdapter<Template, FilterAdapter.FilterViewHolder>(FilterComparator) {

    private val inflater = LayoutInflater.from(context)
    private val imageProcessor = ImageProcessor(context, photo, photo.uri.bitmapSize().scaleToFitHeight(dp(100f)))

    var onItemClickListener: OnItemClickListener<Template>? = null

    private var currentIndex = 0

    inner class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun hasSelected() : Boolean {
        return currentIndex != 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder(inflater.inflate(R.layout.item_filter, parent, false))
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        imageProcessor.blend(holder.itemView.imgPreset, getItem(position))
        holder.itemView.imgSelected.visibility = if (currentIndex == position) View.VISIBLE else View.INVISIBLE
        holder.itemView.setOnClickListener { updateSelected(position) }
    }

    private fun updateSelected(position: Int) {
        val oldPos = currentIndex;
        currentIndex = position
        notifyItemChanged(oldPos)
        notifyItemChanged(currentIndex)
        onItemClickListener?.invoke(getItem(position))
    }

    object FilterComparator : DiffUtil.ItemCallback<Template>() {
        override fun areItemsTheSame(oldItem: Template, newItem: Template): Boolean {
            return oldItem.getId() == newItem.getId()
        }

        override fun areContentsTheSame(oldItem: Template, newItem: Template): Boolean {
            return oldItem == newItem
        }

    }
}