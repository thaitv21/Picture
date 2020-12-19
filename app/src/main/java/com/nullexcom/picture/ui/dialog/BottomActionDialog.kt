package com.nullexcom.picture.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nullexcom.editor.ext.OnItemClickListener
import com.nullexcom.picture.R
import kotlinx.android.synthetic.main.dialog_action_bottom.*
import kotlinx.android.synthetic.main.item_action.view.*

class BottomActionDialog() : BottomSheetDialogFragment() {
    private val adapter = ActionAdapter()
    private var onItemClickListener: OnItemClickListener<Action>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_action_bottom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvActions.adapter = adapter
        rvActions.layoutManager = LinearLayoutManager(context)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<Action>) {
        this.onItemClickListener = onItemClickListener
    }

    fun submitList(actions: List<Action>) {
        adapter.submitList(actions.filter { it.enabled })
    }

    data class Action(val id: Int, val text: String, val icon: Int, var enabled: Boolean = true)

    inner class ActionAdapter : ListAdapter<Action, ActionViewHolder>(ActionComparator()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ActionViewHolder(inflater.inflate(R.layout.item_action, parent, false))
        }

        override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
            holder.apply {
                val action = getItem(position)
                itemView.imgIcon.setImageResource(action.icon)
                itemView.tvAction.text = action.text
                itemView.setOnClickListener { onItemClickListener?.invoke(action) }
            }
        }
    }

    class ActionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ActionComparator : DiffUtil.ItemCallback<Action>() {
        override fun areItemsTheSame(oldItem: Action, newItem: Action): Boolean {
            return oldItem.text == newItem.text
        }

        override fun areContentsTheSame(oldItem: Action, newItem: Action): Boolean {
            return oldItem == newItem
        }

    }
}