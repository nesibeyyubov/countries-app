package com.nesib.countriesapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.nesib.countriesapp.R
import com.nesib.countriesapp.base.BaseFragment
import com.nesib.countriesapp.base.ScreenParams
import com.nesib.countriesapp.databinding.FragmentHomeBinding
import com.nesib.countriesapp.ui.countries.CountriesFragment
import com.nesib.countriesapp.utils.Region
import com.nesib.countriesapp.utils.slideInOutAnimationNavOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeState, HomeViewModel, ScreenParams>() {


    override val viewModel: HomeViewModel
        get() = ViewModelProvider(this)[HomeViewModel::class.java]

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun initViews() = with(binding) {
        africa.setOnClickListener { navigate(R.id.countriesFragment, CountriesFragment.Params(Region.AFRICA)) }
        allCountries.setOnClickListener {
            navigate(
                R.id.countriesFragment,
                CountriesFragment.Params(Region.ALL_COUNTRIES),
                slideInOutAnimationNavOptions
            )
        }
        europe.setOnClickListener {
            navigate(
                R.id.countriesFragment,
                CountriesFragment.Params(Region.EUROPE),
                slideInOutAnimationNavOptions
            )
        }
        africa.setOnClickListener {
            navigate(
                R.id.countriesFragment,
                CountriesFragment.Params(Region.AFRICA),
                slideInOutAnimationNavOptions
            )
        }
        asia.setOnClickListener {
            navigate(
                R.id.countriesFragment, CountriesFragment.Params(Region.ASIA),
                slideInOutAnimationNavOptions
            )
        }
        oceania.setOnClickListener {
            navigate(
                R.id.countriesFragment,
                CountriesFragment.Params(Region.OCEANIA),
                slideInOutAnimationNavOptions
            )
        }
        americas.setOnClickListener {
            navigate(
                R.id.countriesFragment,
                CountriesFragment.Params(Region.AMERICAS),
                slideInOutAnimationNavOptions
            )
        }

//        appBarLayout?.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
//            Log.d("mytag", "offset: $verticalOffset")
//        }


        Unit
    }

    override fun render(state: HomeState) {

    }


}