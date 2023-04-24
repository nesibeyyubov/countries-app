package com.nesib.countriesapp.ui.countries

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nesib.countriesapp.R
import com.nesib.countriesapp.base.BaseFragment
import com.nesib.countriesapp.base.ScreenParams
import com.nesib.countriesapp.databinding.FragmentResultsBinding
import com.nesib.countriesapp.ui.details.CountryDetailsFragment
import com.nesib.countriesapp.utils.Region
import com.nesib.countriesapp.utils.slideInOutAnimationNavOptions
import com.nesib.countriesapp.utils.toTranslatedText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountriesFragment :
    BaseFragment<FragmentResultsBinding, CountriesState, CountriesViewModel, CountriesFragment.Params>() {

    override val viewModel: CountriesViewModel
        get() = ViewModelProvider(this)[CountriesViewModel::class.java]

    private val countriesAdapter by lazy { CountriesAdapter() }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentResultsBinding.inflate(inflater, container, false)

    override fun initViews(): Unit = with(binding) {
        recyclerView.adapter = countriesAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        countriesAdapter.onCountryClick = {
            navigate(R.id.countryDetailsFragment, CountryDetailsFragment.Params(it), slideInOutAnimationNavOptions)
        }
        params?.run {
            regionName.text = this.region.toTranslatedText(requireContext())
            regionNameContainer.background = getDrawable(this.region.drawableRes)
        }

        searchInput.doAfterTextChanged {
            val query = it.toString().trim()
            if (query.isNotEmpty()) viewModel.search(query)
        }

        ibBack.setOnClickListener { navigateBack() }

        singleChipSelector.chipSelectListener = { selectedChip ->
            viewModel.sortCountriesBy(selectedChip)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        params?.region?.run {
            if (this == Region.ALL_COUNTRIES) {
                viewModel.getAllCountries()
            } else {
                viewModel.getCountriesByRegion(this.value)
            }
        }
    }

    override fun render(state: CountriesState) = with(binding) {
        shimmerLayout.isVisible = state.loading
        recyclerView.isVisible = !state.loading

        if (state.error != null) {
            recyclerView.isVisible = false
            shimmerLayout.isVisible = false
            hasFailureText.text = state.error
            hasFailureText.isVisible = true
            return@with
        }

        if (!state.loading) {
            when (state.sortedBy) {
                SingleChipSelector.SortBy.Population -> {
                    singleChipSelector.populationSelected()
                    val countries =
                        state.countries
                            .sortedBy { it.population }
                            .filter { it.name.common.contains(state.searchQuery, ignoreCase = true) }

                    val isCountriesSame = countries == countriesAdapter.currentList
                    countriesAdapter.submitList(countries) {
                        if (!isCountriesSame) recyclerView.scrollToPosition(0)
                    }
                }
                SingleChipSelector.SortBy.Area -> {
                    singleChipSelector.areaSelected()
                    val countries =
                        state.countries
                            .sortedBy { it.area }
                            .filter { it.name.common.contains(state.searchQuery, ignoreCase = true) }

                    val isCountriesSame = countries == countriesAdapter.currentList
                    countriesAdapter.submitList(countries) {
                        if (!isCountriesSame) recyclerView.scrollToPosition(0)
                    }
                }
                SingleChipSelector.SortBy.None -> {
                    singleChipSelector.resetChips()
                    val countries =
                        state.countries
                            .filter { it.name.common.contains(state.searchQuery, ignoreCase = true) }

                    val isCountriesSame = countries == countriesAdapter.currentList
                    countriesAdapter.submitList(countries) {
                        if (!isCountriesSame) recyclerView.scrollToPosition(0)
                    }
                }
            }


        }


    }


    data class Params(val region: Region) : ScreenParams

}
