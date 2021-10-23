package com.nesib.countriesapp.adapters

import android.content.Context
import android.net.Uri
import com.nesib.countriesapp.models.Country
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.nesib.countriesapp.database.DatabaseHelper
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.nesib.countriesapp.R
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import android.widget.TextView
import android.widget.ImageButton
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.nesib.countriesapp.ui.FavoritesFragmentDirections
import java.util.*

class FavoritesChildAdapter(
    private val countryList: MutableList<Country?>,
    val context: Context?,
    val navController: NavController,
    val deleteRow: FavoritesParentAdapter.Callable<String, Void?>
) : RecyclerView.Adapter<FavoritesChildAdapter.ViewHolder>() {
    private val db: DatabaseHelper = DatabaseHelper.getInstance(context)
    private val countryCodes: List<Country>? = null
    private val view: View? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_favorites_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = countryList[position]
        holder.countryName.text = country!!.name
        holder.area.text = country.area.toString() + ""
        holder.population.text = country.population.toString() + ""
        holder.countryFullName.text = country.altSpellings[0]
        GlideToVectorYou
            .init()
            .with(holder.itemView.context)
            .load(Uri.parse(country.flag), holder.flagImage)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var countryName: TextView
        var countryFullName: TextView
        var area: TextView
        var population: TextView
        var heartButton: ImageButton
        var flagImage: ImageView
        var overlay: View

        init {
            countryName = itemView.findViewById(R.id.countryName)
            countryFullName = itemView.findViewById(R.id.countryFullName)
            area = itemView.findViewById(R.id.area)
            population = itemView.findViewById(R.id.population)
            flagImage = itemView.findViewById(R.id.flagImage)
            heartButton = itemView.findViewById(R.id.heartButton)
            overlay = itemView.findViewById(R.id.overlay)
            heartButton.setOnClickListener {
                val currentCountry = countryList[adapterPosition]
                db.deleteCountry(currentCountry!!)
                countryList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                if (countryList.size == 0) {
                    currentCountry.region?.let { it1 -> deleteRow.call(it1) }
                }
                val scaleInAnimation =
                    AnimationUtils.loadAnimation(itemView.context, R.anim.scale_in)
                val scaleOutAnimation =
                    AnimationUtils.loadAnimation(itemView.context, R.anim.scale_out)
                heartButton.startAnimation(scaleInAnimation)
                heartButton.startAnimation(scaleOutAnimation)
            }
            overlay.setOnClickListener {
                val action =
                    FavoritesFragmentDirections.actionNavigationFavoritesToNavigationDetails()
                val currentCountry = countryList[adapterPosition]
                action.setCountry(currentCountry)
                action.fromSearch = true
                navController.navigate(action)
            }
        }
    }

    init {
        Collections.reverse(countryList)
    }
}