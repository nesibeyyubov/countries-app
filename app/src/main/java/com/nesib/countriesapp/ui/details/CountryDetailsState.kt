package com.nesib.countriesapp.ui.details

import androidx.annotation.StringRes
import com.nesib.countriesapp.base.State
import com.nesib.countriesapp.models.BorderUi

data class CountryDetailsState(
    val borders: List<BorderUi> = emptyList(),
    val bordersLoading: Boolean = false,
    @StringRes val error: Int? = null
) : State