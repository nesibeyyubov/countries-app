package com.nesib.countriesapp.ui.countries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nesib.countriesapp.base.BaseFragment
import com.nesib.countriesapp.base.ScreenParams
import com.nesib.countriesapp.databinding.FragmentHomeBinding
import com.nesib.countriesapp.databinding.FragmentResultsBinding
import com.nesib.countriesapp.databinding.FragmentSearchBinding
import com.nesib.countriesapp.utils.Region
import com.nesib.countriesapp.utils.toSafeString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountriesFragment :
    BaseFragment<FragmentResultsBinding, CountriesState, CountriesViewModel, CountriesFragment.Params>() {

    override val viewModel: CountriesViewModel
        get() = ViewModelProvider(this)[CountriesViewModel::class.java]

    private val countriesAdapter by lazy { CountriesAdapter() }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentResultsBinding.inflate(inflater, container, false)

    override fun initViews() = with(binding) {
        recyclerView.adapter = countriesAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        regionName.text = params?.region?.value.toSafeString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        params?.region?.run {
            viewModel.getCountriesByRegion(this.value)
        }
    }

    override fun render(state: CountriesState) = with(binding) {
        shimmerLayout.isVisible = state.loading
        recyclerView.isVisible = !state.loading

        if (!state.loading) {
            countriesAdapter.submitList(state.countries)
        }

    }


    data class Params(val region: Region) : ScreenParams

}
