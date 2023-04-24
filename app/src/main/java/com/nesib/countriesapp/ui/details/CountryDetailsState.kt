package com.nesib.countriesapp.ui.details

import com.nesib.countriesapp.base.State
import com.nesib.countriesapp.models.BorderUi

data class CountryDetailsState(
    val borders: List<BorderUi> = emptyList(),
    val bordersLoading: Boolean = false,
    val error: String? = null
) : State