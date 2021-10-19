package com.nesib.countriesapp.adapters

import android.net.Uri
import android.os.Handler
import com.nesib.countriesapp.models.Country
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.nesib.countriesapp.R
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import android.widget.TextView
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView

class CountryBordersAdapter(private val countryList: List<Country>) :
    RecyclerView.Adapter<CountryBordersAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null
    fun setListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onClick(selectedCountry: Country)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.border_country_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = countryList[position]
        holder.countryName.text = country.name
        GlideToVectorYou
            .init()
            .with(holder.itemView.context)
            .load(Uri.parse(country.flag), holder.flagImage)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var flagImage: ImageView = itemView.findViewById(R.id.borderCountryImage)
        var countryName: TextView = itemView.findViewById(R.id.borderCountryName)

        init {
            itemView.setOnClickListener { view ->
                val scaleOut = AnimationUtils.loadAnimation(view.context, R.anim.scale_out)
                itemView.startAnimation(scaleOut)
                Handler().postDelayed(Runnable {
                    listener!!.onClick(
                        countryList[adapterPosition]
                    )
                }, 200)
            }
        }
    }
}