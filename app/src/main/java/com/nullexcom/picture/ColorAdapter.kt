package com.nullexcom.picture

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nullexcom.editor.ext.OnItemClickListener
import com.nullexcom.picture.ext.toColor
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.item_color.view.*

class ColorAdapter(val context: Context) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {
    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val inflater = LayoutInflater.from(context)
    private val indexObservable = BehaviorSubject.createDefault(0)
    private var currentIndex = 0
    private var disposable: Disposable? = null
    private val gradientColors = listOf(
            intArrayOf("C41432".toColor(), "FC5A44".toColor()),
            intArrayOf("4DD750".toColor(), "C0FF8B".toColor()),
            intArrayOf("085FE5".toColor(), "3ED0FE".toColor())
    )

    fun setOnItemSelected(onPageSelected: (Int) -> Unit) {
        disposable?.dispose()
        disposable = indexObservable.doOnNext { onPageSelected.invoke(currentIndex) }.subscribe()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(inflater.inflate(R.layout.item_color, parent, false))
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val isSelected = currentIndex == position
        holder.itemView.imgColor.setColors(gradientColors[position])
        holder.itemView.setOnClickListener { onChanged(position) }
        holder.itemView.imgSelected.visibility = if (isSelected) View.VISIBLE else View.GONE
    }

    private fun onChanged(position: Int) {
        if (currentIndex == position) return
        val oldPos = currentIndex
        currentIndex = position
        notifyItemChanged(oldPos)
        notifyItemChanged(currentIndex)
        indexObservable.onNext(currentIndex)
    }
}