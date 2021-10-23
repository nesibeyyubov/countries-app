package com.nesib.countriesapp.adapters

import android.content.Context
import android.net.Uri
import com.nesib.countriesapp.models.Country
import androidx.recyclerview.widget.RecyclerView
import com.nesib.countriesapp.database.DatabaseHelper
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.nesib.countriesapp.R
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import androidx.core.content.res.ResourcesCompat
import android.widget.ImageButton
import android.widget.TextView
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import java.lang.Exception
import java.text.DecimalFormat
import java.util.ArrayList

class SearchResultAdapter() :
    RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {
    private var countryList: List<Country> = ArrayList()
    private var listener: OnItemClickListener? = null
//    private val db: DatabaseHelper = DatabaseHelper.getInstance(context)
//    fun setCountryCodes(countryCodes: List<Country>) {
//        this.countryCodes = countryCodes
//    }

    fun setCountryList(countryList: List<Country>) {
        this.countryList = countryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        view =
            LayoutInflater.from(parent.context).inflate(R.layout.country_search_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = countryList[position]
        holder.countryName.text = country.name
        holder.capitalName.text = country.capital
        var area = java.lang.Double.toString(country.area)
        area = area.substring(0, area.length - 2)
        holder.area.text = area
        val decimalFormat = DecimalFormat("#,##0.###")
        holder.population.text = decimalFormat.format(country.population)
        try {
            GlideToVectorYou
                .init()
                .with(holder.itemView.context)
                .load(Uri.parse(country.flag), holder.flagImage)
            holder.flagImage.transitionName = country.flag
        } catch (exception: Exception) {
        }
//        if (isFavorite(country)) {
//            holder.heartButton.setImageDrawable(
//                ResourcesCompat.getDrawable(
//                    context.resources,
//                    R.drawable.ic_favorites_filled,
//                    null
//                )
//            )
//        } else {
//            holder.heartButton.setImageDrawable(
//                ResourcesCompat.getDrawable(
//                    context.resources,
//                    R.drawable.ic_favorites_outline,
//                    null
//                )
//            )
//        }
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var flagImage: ImageView = itemView.findViewById(R.id.flagImage)
        var heartButton: ImageButton = itemView.findViewById(R.id.heartButton)
        var countryName: TextView = itemView.findViewById(R.id.countryName)
        var capitalName: TextView = itemView.findViewById(R.id.capitalName)
        var regionName: TextView? = null
        var population: TextView = itemView.findViewById(R.id.population)
        var area: TextView = itemView.findViewById(R.id.area)

        init {
            itemView.setOnClickListener {
                listener!!.onClick(
                    countryList[adapterPosition]
                )
            }
//            heartButton.setOnClickListener { view ->
//                listener!!.onHeartClick(adapterPosition)
//                if (isFavorite(countryList[adapterPosition])) {
//                    heartButton.setImageDrawable(view.context.getDrawable(R.drawable.ic_favorites_outline))
//                } else {
//                    heartButton.setImageDrawable(view.context.getDrawable(R.drawable.ic_favorites_filled))
//                }
//                val scaleInAnimation =
//                    AnimationUtils.loadAnimation(itemView.context, R.anim.scale_in)
//                val scaleOutAnimation =
//                    AnimationUtils.loadAnimation(itemView.context, R.anim.scale_out)
//                heartButton.startAnimation(scaleInAnimation)
//                heartButton.startAnimation(scaleOutAnimation)
//                countryCodes = db.countryCodes
//            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onClick(country: Country)
        fun onHeartClick(country: Country)
    }

//    fun isFavorite(currentCountry: Country): Boolean {
//        var flag = false
//        for (code: Country in countryCodes) {
//            if ((currentCountry.flag == code.flag)) {
//                flag = true
//                break
//            }
//        }
//        return flag
//    }

}