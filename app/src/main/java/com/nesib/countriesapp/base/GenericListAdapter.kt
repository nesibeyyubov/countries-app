package com.nesib.countriesapp.base

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class GenericListAdapter<Binding : ViewBinding, Model>(
    val inflate: (Context, ViewGroup, Boolean) -> Binding,
    val onBind: (Model, Int, Binding) -> Unit
) :
    RecyclerView.Adapter<GenericListAdapter.GenericViewHolder<Binding>>() {

    private val items = mutableListOf<Model>()

    fun submitItems(newItems: List<Model>) {
        this.items.clear()
        this.items.addAll(newItems)
        notifyDataSetChanged()
    }

    class GenericViewHolder<Binding : ViewBinding>(val binding: Binding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<Binding> {
        return GenericViewHolder(inflate(parent.context, parent, false))
    }

    override fun onBindViewHolder(holder: GenericViewHolder<Binding>, position: Int) {
        onBind(items[position], position, holder.binding)
    }

    override fun getItemCount() = items.size
}