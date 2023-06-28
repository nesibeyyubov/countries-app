package com.nesib.countriesapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.nesib.countriesapp.models.CountryUi

class CountryDiffUtil : DiffUtil.ItemCallback<CountryUi>() {
    override fun areItemsTheSame(oldItem: CountryUi, newItem: CountryUi): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: CountryUi, newItem: CountryUi): Boolean {
        return oldItem == newItem
    }

}