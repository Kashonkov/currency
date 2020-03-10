package com.exmpale.ui.base

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class BaseRecyclerAdapter<M, VH : BaseRecyclerAdapter.ViewHolder<M>> :
    RecyclerView.Adapter<VH>() {
    internal val items = ArrayList<M>()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindHolder(items[position])
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: List<Any>) {
        holder.bindHolder(items[position], payloads)
    }

    override fun onViewRecycled(holder: VH) {
        holder.unbindHolder()
    }

    fun getItems(): List<M> {
        return items
    }

    open fun getDiffCallBack(oldItems: List<M>, newItems: List<M>): DiffUtil.Callback? {
        return null
    }

    open fun setItems(items: List<M>) {
        val diffCallback = getDiffCallBack(this.items, items)
        if (diffCallback == null) {
            setNewData(items)
            notifyDataSetChanged()
        } else {
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            setNewData(items)
            diffResult.dispatchUpdatesTo(this)
        }
    }

    open fun getItem(position: Int): M {
        return items[position]
    }

    protected fun setNewData(data: List<M>) {
        this.items.clear()
        this.items.addAll(data)
    }

    open fun clearItems() {
        val count = itemCount
        items.clear()
        notifyItemRangeRemoved(0, count)
    }

    abstract class ViewHolder<M>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context: Context
            get() = itemView.context

        abstract fun bindHolder(model: M)

        open fun bindHolder(model: M, payloads: List<Any>) {
            bindHolder(model)
        }

        open fun unbindHolder() {
        }
    }
}