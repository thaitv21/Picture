package com.nullexcom.picture

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nullexcom.editor.ext.OnItemClickListener
import kotlinx.android.synthetic.main.item_menu.view.*

class MenuAdapter(private val context: Context, val items: List<Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    private var onItemClickListener: (OnItemClickListener<Int>)? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<Int>) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_menu, parent, false)) {}
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.imgIcon.setImageResource(items[position].icon)
        holder.itemView.tvLabel.text = items[position].text
        holder.itemView.setOnClickListener { onItemClickListener?.invoke(position) }
    }

    data class Item(
            val id: Int,
            val text: String,
            val icon: Int
    )
}