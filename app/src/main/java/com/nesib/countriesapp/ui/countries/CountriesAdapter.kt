package com.nesib.countriesapp.ui.countries

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.nesib.countriesapp.base.BaseRecyclerAdapter
import com.nesib.countriesapp.databinding.CountrySearchItemBinding
import com.nesib.countriesapp.models.CountryUi
import com.nesib.countriesapp.utils.CountryDiffUtil

class CountriesAdapter : BaseRecyclerAdapter<CountryUi, CountrySearchItemBinding>(CountryDiffUtil()) {
    override fun viewHolder(parent: ViewGroup, viewType: Int): ViewHolder<CountryUi, CountrySearchItemBinding> {
        return CountryViewHolder(CountrySearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    inner class CountryViewHolder(val binding: CountrySearchItemBinding) :
        ViewHolder<CountryUi, CountrySearchItemBinding>(binding) {

        init {
            binding.root.setOnClickListener {

            }
        }

        override fun bindData(data: CountryUi) {
            with(binding) {
                countryName.text = data.name.common
                flagImage.load(data.flags.pngFormat)
                capitalName.text = data.capital.joinToString("/")
                population.text = data.population.toString()
                area.text = data.area.toString()
            }
        }

    }


}


