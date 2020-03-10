package com.exmpale.ui.base

import androidx.recyclerview.widget.DiffUtil

abstract class BaseDiffUtilCallback<T>(
    val oldItems: List<T>,
    val newItems: List<T>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame(oldItems[oldItemPosition], newItems[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areContentsTheSame(oldItems[oldItemPosition], newItems[newItemPosition])
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return getChangePayload(oldItems[oldItemPosition], newItems[newItemPosition])
    }

    open fun getChangePayload(oldItem: T, newItem: T): Any? {
        return null
    }

    abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean
    abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean
}