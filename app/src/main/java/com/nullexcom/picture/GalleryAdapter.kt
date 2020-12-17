package com.nullexcom.picture

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nullexcom.editor.data.Photo
import com.nullexcom.editor.ext.screenWidth
import kotlinx.android.synthetic.main.item_gallery.view.*

class GalleryAdapter(val context: Context) : RecyclerView.Adapter<GalleryAdapter.PhotoViewHolder>() {

    private val photos = mutableListOf<Photo>()
    private var onItemClickListener: ((Photo) -> Unit)? = null
    private var onItemLongClickListener: ((Photo) -> Unit)? = null
    private var selectedIndex = -1

    fun setData(photos: List<Photo>) {
        this.photos.clear()
        this.photos.addAll(photos)
        this.notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: ((Photo) -> Unit)?) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: (Photo) -> Unit) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(LayoutInflater.from(context).inflate(R.layout.item_gallery, parent, false))
    }

    override fun getItemCount() = photos.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(i: Int) {
            adjustLayout(i)
            val photo = photos[i]
            Glide.with(context).load(photo.uri).into(itemView.imgPhoto)
            itemView.viewEffect.visibility = if (selectedIndex == i) View.VISIBLE else View.GONE
            itemView.setOnClickListener { onClick(i) }
            itemView.setOnLongClickListener {
                onItemLongClickListener?.invoke(photos[i])
                return@setOnLongClickListener true
            }
        }

        private fun adjustLayout(position: Int) {
            val horizontalPadding = (context.resources.getDimension(R.dimen.paddingHorizontal)).toInt()
            val width = ((context.screenWidth - horizontalPadding * 3) / 2)
            val height = when (position % 8) {
                0 -> width * 1.3f
                1 -> width * 1.5f
                2 -> width * 1.5f
                3 -> width * 1.5f
                4 -> width * 1.5f
                5 -> width * 1.8f
                6 -> width * 1.8f
                else -> (width * 1.3f)
            }
            val params = itemView.layoutParams as RecyclerView.LayoutParams
            params.height = height.toInt()
            if (position % 2 == 0) {
                params.marginStart = horizontalPadding
                params.marginEnd = horizontalPadding / 2
            } else {
                params.marginStart = horizontalPadding / 2
                params.marginEnd = horizontalPadding
            }
            itemView.layoutParams = params
        }

        private fun onClick(i: Int) {
            if (selectedIndex > -1) {
                val oldPos = selectedIndex
                selectedIndex = -1
                notifyItemChanged(oldPos)
            }
            onItemClickListener?.invoke(photos[i])
        }
    }

    object PhotoComparator : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.uri == newItem.uri
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }

    }
}