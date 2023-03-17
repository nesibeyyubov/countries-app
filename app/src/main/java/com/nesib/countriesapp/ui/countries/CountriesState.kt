package com.nesib.countriesapp.ui.countries

import com.nesib.countriesapp.base.State
import com.nesib.countriesapp.models.CountryUi

data class CountriesState(
    val countries: List<CountryUi> = emptyList(),
    val loading: Boolean = false,
    val sortedBy: SingleChipSelector.SortBy = SingleChipSelector.SortBy.None
) : State
