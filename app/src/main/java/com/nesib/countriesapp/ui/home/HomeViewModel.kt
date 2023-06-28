package com.nesib.countriesapp.ui.home

import com.nesib.countriesapp.base.BaseViewModel
import com.nesib.countriesapp.data.network.CountriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val countriesRepository: CountriesRepository
) : BaseViewModel<HomeState>(HomeState())