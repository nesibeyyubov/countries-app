package com.nesib.countriesapp.ui

import android.net.Uri
import com.nesib.countriesapp.models.Country
import androidx.recyclerview.widget.RecyclerView
import com.nesib.countriesapp.database.DatabaseHelper
import androidx.navigation.NavController
import androidx.core.widget.NestedScrollView
import android.os.Bundle
import android.view.View
import android.widget.*
import com.nesib.countriesapp.R
import androidx.lifecycle.ViewModelProvider
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.nesib.countriesapp.adapters.CountryBordersAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.nesib.countriesapp.utils.DataState
import com.nesib.countriesapp.viewmodels.CountriesViewModel
import java.lang.StringBuilder
import java.text.DecimalFormat
import java.util.ArrayList

class DetailsFragment : Fragment(R.layout.fragment_country_details), View.OnClickListener {
    private lateinit var country: Country
    private lateinit var countryName: TextView
    private lateinit var noBorders: TextView
    private lateinit var countryFullName: TextView
    private lateinit var currency: TextView
    private lateinit var regionName: TextView
    private lateinit var capitalName: TextView
    private lateinit var population: TextView
    private lateinit var area: TextView
    private lateinit var language: TextView
    private lateinit var callingCodes: TextView
    private lateinit var flagImage: ImageView
    private lateinit var backButton: ImageButton
    private lateinit var heartButton: ImageButton
    private lateinit var bordersRecyclerView: RecyclerView
    private lateinit var db: DatabaseHelper
    private lateinit var countryCodes: List<Country>
    private lateinit var bordersProgressBar: ProgressBar
    private lateinit var showMapButton: LinearLayout
    private lateinit var navController: NavController
    private lateinit var nestedScrollView: NestedScrollView

    private lateinit var countriesViewModel: CountriesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flagImage = view.findViewById(R.id.flagImage)
        countryName = view.findViewById(R.id.countryName)
        countryFullName = view.findViewById(R.id.countryFullName)
        currency = view.findViewById(R.id.currency)
        heartButton = view.findViewById(R.id.heartButton)
        capitalName = view.findViewById(R.id.capitalName)
        population = view.findViewById(R.id.population)
        area = view.findViewById(R.id.area)
        language = view.findViewById(R.id.language)
        callingCodes = view.findViewById(R.id.callingCode)
        backButton = view.findViewById(R.id.backButton)
        noBorders = view.findViewById(R.id.noBorders)
        bordersProgressBar = view.findViewById(R.id.bordersProgressBar)
        bordersRecyclerView = view.findViewById(R.id.bordersRecyclerView)
        showMapButton = view.findViewById(R.id.showMapButton)
        regionName = view.findViewById(R.id.regionName)
        nestedScrollView = view.findViewById(R.id.nestedScrollView)
        navController = Navigation.findNavController(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        countriesViewModel = ViewModelProvider(this).get(
            CountriesViewModel::class.java
        )
        db = DatabaseHelper.getInstance(activity)
        if (arguments != null) {
            country = DetailsFragmentArgs.fromBundle(
                requireArguments()
            ).country!!
            setupUi()
            backButton.setOnClickListener(this)
            heartButton.setOnClickListener(this)
            showMapButton.setOnClickListener(this)
        }
    }

    private fun setupUi() {
        countryCodes = db.countryCodes
        for (code in countryCodes as ArrayList<Country>) {
            if (code.flag == country.flag) {
                heartButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_favorites_filled,
                        null
                    )
                )
            }
        }
        GlideToVectorYou
            .init()
            .with(activity)
            .load(Uri.parse(country.flag), flagImage)
        countryName.text = country.name
        if (country.capital == null || country.capital?.length == 0) {
            capitalName.text =
                getString(R.string.capital_text) + "  " + getString(R.string.no_information_text)
        } else {
            capitalName.text = getString(R.string.capital_text) + "  " + country!!.capital
        }
        population.text = "" + country!!.population
        flagImage.transitionName = country.flag
        val decimalFormat = DecimalFormat("#,##0.###")
        population.text = decimalFormat.format(country.population)
        var areaText = java.lang.Double.toString(country.area)
        areaText = areaText.substring(0, areaText.length - 2)
        area.text = areaText
        val callingCodesArray = country.callingCodes
        val latlngArray = country.latlng
        val bordersArray = country.borders
        val currenciesArray = country.currencies
        val languagesArray = country.languages
        val countryFullNamesArray = country.altSpellings
        val borderText = StringBuilder()
        val languageText = StringBuilder()
        for (i in languagesArray.indices) {
            languageText.append(languagesArray[i].name).append(",")
        }
        languageText.deleteCharAt(languageText.length - 1)
        if (callingCodesArray[0] != null && callingCodesArray[0]!!.length > 0) {
            callingCodes.text = "+" + callingCodesArray[0]
        } else {
            callingCodes.text = getString(R.string.no_information_text)
        }

        // Sometimes there can be only 1 full name in rest api response
        if (countryFullNamesArray.size > 1) {
            countryFullName.text = countryFullNamesArray[1]
        } else {
            countryFullName.text = country.name
        }
        language.text = languageText
        if (currenciesArray.isNotEmpty()) {
            currency.text = currenciesArray[0].name
        }
        for (i in bordersArray.indices) {
            borderText.append(bordersArray[i]).append(",")
        }
        if (borderText.isNotEmpty()) {
            borderText.deleteCharAt(borderText.length - 1)
        } else {
            borderText.append(getString(R.string.no_borders_text))
        }
        if (country.region.isNullOrEmpty()) {
            regionName.setText(R.string.no_information_text)
        } else {
            regionName.text = country.region
        }
        setupCountryBorders(bordersArray)
    }

    private fun setupCountryBorders(bordersArray: List<String>?) {
        val borderCountries = mutableListOf<Country>()
        countriesViewModel.getAllCountries()
        countriesViewModel.allCountries.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    bordersProgressBar.visibility = View.GONE
                    if (bordersArray != null) {
                        for (country in it.data ?: emptyList()) {
                            for (border in bordersArray) {
                                if (country.alpha3Code == border) {
                                    borderCountries.add(country)
                                    break
                                }
                            }
                        }
                    }
                    if (borderCountries.isEmpty()) {
                        noBorders.visibility = View.VISIBLE
                        noBorders.setText(R.string.no_borders_text)
                    }
                    val bordersAdapter = CountryBordersAdapter(borderCountries)
                    bordersRecyclerView.apply {
                        layoutManager =
                            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                        adapter = bordersAdapter
                    }
                    bordersAdapter.setListener(object : CountryBordersAdapter.OnItemClickListener {
                        override fun onClick(selectedCountry: Country) {
                            country = selectedCountry
                            nestedScrollView.fullScroll(View.FOCUS_UP)
                            setupUi()
                        }
                    })
                }
                is DataState.Loading -> {
                    bordersProgressBar.visibility = View.VISIBLE
                }
                is DataState.Error -> {
                    bordersProgressBar.visibility = View.GONE
                    noBorders.visibility = View.VISIBLE
                    noBorders.text = getString(R.string.something_went_wrong)
                    noBorders.setTextColor(resources.getColor(R.color.colorRed))
                }
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.backButton -> navController!!.popBackStack()
            R.id.heartButton -> if (isFavorite) {
                country.let { db.deleteCountry(it) }
                heartButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_favorites_outline,
                        null
                    )
                )
            } else {
                country.let { db.addCountry(it) }
                heartButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_favorites_filled,
                        null
                    )
                )
            }
            R.id.showMapButton -> {
                val action =
                    DetailsFragmentDirections.actionNavigationDetailsToMapFragment()
                val latlng = country.latlng
                val lat = latlng[0].toFloat()
                val lng = latlng[1].toFloat()
                val latLngArgument = floatArrayOf(lat, lng)
                action.latLng = latLngArgument
                navController.navigate(action)
            }
        }
    }

    val isFavorite: Boolean
        get() {
            countryCodes = db.countryCodes
            var flag = false
            for (code in countryCodes as ArrayList<Country>) {
                if (country.flag == code.flag) {
                    flag = true
                    break
                }
            }
            return flag
        }
}