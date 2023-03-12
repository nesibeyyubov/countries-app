package com.nesib.countriesapp.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


abstract class BaseRecyclerAdapter<T, VB : ViewBinding>() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<T>()

    fun submitItems(newItems: List<T>) {
        this.items.clear()
        this.items.addAll(newItems)
        notifyDataSetChanged()
    }

    abstract class ViewHolder<T, VB : ViewBinding>(binding: VB) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bindData(data: T)
    }

    override fun getItemCount() = items.size

    abstract fun viewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T, VB>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return viewHolder(parent, viewType)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ViewHolder<T, VB>)?.bindData(items[position])
    }



}