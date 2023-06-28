package com.nesib.countriesapp.ui.details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.nesib.countriesapp.R
import com.nesib.countriesapp.base.GenericListAdapter
import com.nesib.countriesapp.databinding.CountryDetailItemViewBinding
import com.nesib.countriesapp.models.CountryDetail

class CountryDetailsAdapter : GenericListAdapter<CountryDetailItemViewBinding, CountryDetail>(
    inflate = { context: Context, viewGroup: ViewGroup, b: Boolean ->
        val view = LayoutInflater.from(context).inflate(R.layout.country_detail_item_view, viewGroup, b)
        CountryDetailItemViewBinding.bind(view)
    },
    onBind = { countryDetail, _, binding ->
        with(binding) {
            tvKey.text = binding.root.context.getString(countryDetail.key)
            tvValue.text = countryDetail.value
        }
    }
)

