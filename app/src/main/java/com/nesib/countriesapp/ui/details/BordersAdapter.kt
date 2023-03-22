package com.nesib.countriesapp.ui.details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.nesib.countriesapp.base.GenericListAdapter
import com.nesib.countriesapp.databinding.BorderCountryItemBinding
import com.nesib.countriesapp.models.BorderUi
import com.nesib.countriesapp.models.CountryUi

class BordersAdapter : GenericListAdapter<BorderCountryItemBinding, BorderUi>(
    inflate = { context: Context, viewGroup: ViewGroup, b: Boolean ->
        BorderCountryItemBinding.inflate(LayoutInflater.from(context), viewGroup, false)
    },
    onBind = { model, index, binding ->
        binding.borderCountryName.text = model.name
        binding.borderCountryImage.load(model.flag)
    }
)