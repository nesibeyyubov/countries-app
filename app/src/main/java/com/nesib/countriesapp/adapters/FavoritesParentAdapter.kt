package com.nesib.countriesapp.adapters

import com.nesib.countriesapp.models.FavoriteCountry
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.nesib.countriesapp.R
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.TextView

class FavoritesParentAdapter(
    private val favoriteCountries: MutableList<FavoriteCountry>,
    private val navController: NavController
) : RecyclerView.Adapter<FavoritesParentAdapter.ViewHolder>() {
    private var favoritesChildAdapter: FavoritesChildAdapter? = null
    private var listener: OnAllItemsDeletedListener? = null
    fun setListener(listener: OnAllItemsDeletedListener?) {
        this.listener = listener
    }

    interface OnAllItemsDeletedListener {
        fun onAllDeleted()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_favorites_row, parent, false)
        return ViewHolder(view)
    }

    interface Callable<String, Void> {
        fun call(regionName: String): Void
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteCountry = favoriteCountries[position]
        val countries = favoriteCountry.countryList
        val regionName = favoriteCountry.regionName
        holder.regionName.text = capitalizeString(regionName)
        val deleteRow: Callable<String, Void?> = object : Callable<String, Void?> {
            override fun call(regionName: String): Void? {
                return deleteRow(regionName.toLowerCase())
            }
        }
        favoritesChildAdapter = FavoritesChildAdapter(
            countries.toMutableList(),
            holder.itemView.context.applicationContext,
            navController,
            deleteRow
        )
        holder.childRecyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(holder.itemView.context)
        layoutManager.orientation = RecyclerView.HORIZONTAL
        holder.childRecyclerView.layoutManager = layoutManager
        holder.childRecyclerView.adapter = favoritesChildAdapter
    }

    private fun capitalizeString(text: String): String {
        var newText = text
        newText = text.substring(0, 1).toUpperCase() + text.substring(1)
        return newText
    }

    fun deleteRow(regionName: String): Void? {
        var indexToDelete = 0
        for (i in favoriteCountries.indices) {
            val favoriteCountry = favoriteCountries[i]
            if (favoriteCountry.regionName == regionName.toLowerCase()) {
                indexToDelete = i
                break
            }
        }
        favoriteCountries.removeAt(indexToDelete)
        if (favoriteCountries.isEmpty()) {
            listener!!.onAllDeleted()
        }
        notifyItemRemoved(indexToDelete)
        return null
    }

    override fun getItemCount(): Int {
        return favoriteCountries.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val childRecyclerView: RecyclerView = itemView.findViewById(R.id.childRecyclerView)
        val regionName: TextView = itemView.findViewById(R.id.regionName)

    }
}