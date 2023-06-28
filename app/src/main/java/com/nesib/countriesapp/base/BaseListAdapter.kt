package com.nesib.countriesapp.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


abstract class BaseListAdapter<T, VB : ViewBinding>(diffUtil: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, BaseListAdapter.ViewHolder<T, VB>>(diffUtil) {


    abstract class ViewHolder<T, VB : ViewBinding>(binding: VB) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bindData(data: T)
    }

    abstract fun viewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T, VB>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T, VB> {
        return viewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder<T, VB>, position: Int) {
        holder.bindData(getItem(position))
    }

}