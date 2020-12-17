package com.nullexcom.picture

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nullexcom.editor.data.Preset
import com.nullexcom.editor.ext.OnItemClickListener
import kotlinx.android.synthetic.main.item_filter.view.*

class FilterAdapter(val context: Context, val bitmap: Bitmap?) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private val filters = mutableListOf<Preset>()

    var onItemClickListener: OnItemClickListener<Bitmap>? = null

    private var currentIndex = 0

    inner class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    fun changeData(filters: List<Preset>) {
        this.filters.clear()
        this.filters.addAll(filters)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder(inflater.inflate(R.layout.item_filter, parent, false))
    }

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        bitmap?.apply {
            Glide.with(context).load(bitmap).into(holder.itemView.imgPreset)
            holder.itemView.imgSelected.visibility = if (currentIndex == position) View.VISIBLE else View.INVISIBLE
            holder.itemView.setOnClickListener { updateSelected(position) }
        }
    }

    private fun updateSelected(position: Int) {
        val oldPos = currentIndex;
        currentIndex = position
        notifyItemChanged(oldPos)
        notifyItemChanged(currentIndex)
    }
}