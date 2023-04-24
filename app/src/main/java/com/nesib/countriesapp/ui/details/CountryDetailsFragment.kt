package com.nesib.countriesapp.ui.details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.nesib.countriesapp.R
import com.nesib.countriesapp.base.BaseFragment
import com.nesib.countriesapp.base.ScreenParams
import com.nesib.countriesapp.databinding.FragmentCountryDetailsBinding
import com.nesib.countriesapp.models.CountryUi
import com.nesib.countriesapp.models.toDetailsKeyValue
import com.nesib.countriesapp.ui.details.map.DetailsMapFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CountryDetailsFragment :
    BaseFragment<FragmentCountryDetailsBinding, CountryDetailsState, CountryDetailsViewModel, CountryDetailsFragment.Params>() {

    override val viewModel: CountryDetailsViewModel
        get() = ViewModelProvider(this)[CountryDetailsViewModel::class.java]

    private val detailsAdapter by lazy { CountryDetailsAdapter() }

    private val bordersAdapter by lazy { BordersAdapter() }

    data class Params(val country: CountryUi) : ScreenParams

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCountryDetailsBinding.inflate(inflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        params?.country?.let {
            viewModel.getBorders(it.borders)
        }
    }

    override fun initViews(): Unit = with(binding) {
        makeFragmentFullScreen()

        rvDetails.adapter = detailsAdapter
        bordersRecyclerView.adapter = bordersAdapter
        bordersRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        params?.let {
            val country = it.country
            ivFlag.load(country.flags.pngFormat)
            if (country.coatOfArms.pngFormat.isEmpty()) {
                ivCoatArms.isVisible = false
            } else {
                ivCoatArms.load(country.coatOfArms.pngFormat) {
                    listener(
                        onStart = {
                            photoShimmer.isVisible = true
                            ivCoatArms.isVisible = false
                        },
                        onSuccess = { _, _ ->
                            photoShimmer.isVisible = false
                            ivCoatArms.isVisible = true
                        },
                        onError = { _, _ ->
                            photoShimmer.isVisible = false
                            ivCoatArms.isVisible = false
                        }
                    )
                }
            }


            tvCountryName.text = country.name.common
            tvCountryFullName.text = country.name.official
            tvCountryFullName.text = country.name.official
            tvCapital.text = country.capital.firstOrNull()
            tvArea.text = country.area.toString()
            tvPopulation.text = country.population.toString()

            detailsAdapter.submitItems(country.toDetailsKeyValue())

            tvFlagDescription.isVisible = country.flags.description.isNotEmpty()
            tvFlagDescription.isSelected = true
            tvFlagDescription.text = country.flags.description

            noBorders.isVisible = params?.country?.borders.isNullOrEmpty().not()

            showMapButton.setOnClickListener {
                navigate(
                    R.id.detailsMapFragment,
                    DetailsMapFragment.Params(country.latlng, country.name.common)
                )
            }

        }

        backButton.setOnClickListener { navigateBack() }

    }

    override fun render(state: CountryDetailsState) = with(binding) {
        if (state.error != null) {
            bordersProgressBar.isVisible = false
            noBorders.isVisible = true
            noBorders.setTextColor(Color.RED)
            noBorders.text = state.error
            bordersRecyclerView.isVisible = false
            return@with
        }
        bordersProgressBar.isVisible = state.bordersLoading
        noBorders.isVisible = state.borders.isEmpty() && !state.bordersLoading

        if (state.borders.isNotEmpty()) {
            bordersAdapter.submitItems(state.borders)
        }
    }

    override fun onResume() {
        super.onResume()
        changeStatusBarIconColor(iconsShouldBeLight = true)
    }

    override fun onStop() {
        super.onStop()
        changeStatusBarIconColor(iconsShouldBeLight = false)
    }
}