package com.nesib.countriesapp.ui.details

import android.telecom.Call.Details
import android.view.LayoutInflater
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import coil.load
import com.nesib.countriesapp.R
import com.nesib.countriesapp.base.BaseFragment
import com.nesib.countriesapp.base.BaseViewModel
import com.nesib.countriesapp.base.ScreenParams
import com.nesib.countriesapp.databinding.FragmentCountryDetailsBinding
import com.nesib.countriesapp.models.CountryUi
import com.nesib.countriesapp.models.toDetailsKeyValue
import com.nesib.countriesapp.ui.details.map.DetailsMapFragment


class CountryDetailsFragment :
    BaseFragment<FragmentCountryDetailsBinding, CountryDetailsState, BaseViewModel<CountryDetailsState>, CountryDetailsFragment.Params>() {

    override val viewModel: BaseViewModel<CountryDetailsState>?
        get() = null

    private val detailsAdapter by lazy { CountryDetailsAdapter() }

    data class Params(val country: CountryUi) : ScreenParams

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCountryDetailsBinding.inflate(inflater)

    private fun initStatusBarColors() = with(binding) {
        root.systemUiVisibility =
            SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        val window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = getColor(R.color.transparent)
    }

    override fun initViews(): Unit = with(binding) {
        initStatusBarColors()

        rvDetails.adapter = detailsAdapter
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

            showMapButton.setOnClickListener {
                navigate(
                    R.id.detailsMapFragment,
                    DetailsMapFragment.Params(country.latlng, country.name.common)
                )
            }

        }

        backButton.setOnClickListener { navigateBack() }

    }

    override fun render(state: CountryDetailsState) {

    }

    override fun onResume() {
        super.onResume()
        changeStatusBarIconColor(iconsIsLight = true)
    }

    override fun onStop() {
        super.onStop()
        changeStatusBarIconColor(iconsIsLight = false)
    }
}