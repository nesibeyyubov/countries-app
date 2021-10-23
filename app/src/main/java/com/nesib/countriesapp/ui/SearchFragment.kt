package com.nesib.countriesapp.ui

import android.graphics.Color
import com.nesib.countriesapp.models.Country
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import androidx.navigation.NavController
import com.nesib.countriesapp.adapters.SearchResultAdapter
import com.nesib.countriesapp.database.DatabaseHelper
import android.os.Bundle
import com.nesib.countriesapp.R
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.nesib.countriesapp.utils.DataState
import com.nesib.countriesapp.utils.SortOptions
import com.nesib.countriesapp.viewmodels.CountriesViewModel

class SearchFragment : Fragment(R.layout.fragment_search), View.OnClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchInput: EditText
    private lateinit var shimmerView: ShimmerFrameLayout
    private lateinit var shimmerLayout: RelativeLayout
    private lateinit var navController: NavController
    private lateinit var countryNotFound: TextView
    private lateinit var searchIcon: ImageView

    private lateinit var countryCodes: List<Country>
    private lateinit var sortPopulationButton: Button
    private lateinit var sortAreaButton: Button
    private lateinit var filterContainer: LinearLayout

    private val countriesViewModel: CountriesViewModel by viewModels()
    private var searchText = ""
    private var sortOption = SortOptions.None
    private val searchResultAdapter by lazy { SearchResultAdapter() }
    private lateinit var db: DatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        searchInput = view.findViewById(R.id.searchInput)
        shimmerLayout = view.findViewById(R.id.shimmerLayout)
        shimmerView = view.findViewById(R.id.shimmer_view_container)
        countryNotFound = view.findViewById(R.id.countryNotFound)
        sortPopulationButton = view.findViewById(R.id.sort_population)
        sortAreaButton = view.findViewById(R.id.sort_area)
        filterContainer = view.findViewById(R.id.filterContainer)
        searchIcon = view.findViewById(R.id.searchIcon)

        navController = Navigation.findNavController(view)
        setupUi()
        setOnClickListeners()
        setOnSearchInputChangeListener()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        countriesViewModel.countriesByName.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    it.data?.let {
                        when (sortOption) {
                            SortOptions.None -> {
                                toggleSortButton(
                                    buttonToUnFocus = sortPopulationButton,
                                    buttonToUnFocus2 = sortAreaButton
                                )
                                searchResultAdapter.setCountryList(it)
                            }
                            SortOptions.Population -> {
                                countriesViewModel.sortCountriesByPopulation()
                            }
                            SortOptions.Area -> {
                                countriesViewModel.sortCountriesByArea()
                            }
                        }
                    }
                    shimmerLayout.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    countryNotFound.visibility = View.GONE
                    filterContainer.visibility = View.VISIBLE
                }
                is DataState.Loading -> {
                    shimmerLayout.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    countryNotFound.visibility = View.GONE
                    filterContainer.visibility = View.GONE
                }
                is DataState.Error -> {
                    filterContainer.visibility = View.GONE
                    shimmerLayout.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    countryNotFound.visibility = View.VISIBLE
                    countryNotFound.text = it.message
                }
            }
        }
        countriesViewModel.countriesSortedByArea.observe(viewLifecycleOwner) {
            if (it != null) {
                searchResultAdapter.setCountryList(it)
                toggleSortButton(
                    buttonToFocus = sortAreaButton,
                    buttonToUnFocus = sortPopulationButton
                )
            }
        }
        countriesViewModel.countriesSortedByPopulation.observe(viewLifecycleOwner) {
            if (it != null) {
                searchResultAdapter.setCountryList(it)
                toggleSortButton(
                    buttonToFocus = sortPopulationButton,
                    buttonToUnFocus = sortAreaButton
                )

            }
        }
    }

    private fun setOnSearchInputChangeListener() {
        searchInput.addTextChangedListener {
            if (!it?.toString().isNullOrEmpty() && searchText != it.toString().toLowerCase()) {
                searchIcon.setImageResource(R.drawable.ic_close)
                countriesViewModel.getCountriesByName(it.toString().toLowerCase())
                searchText = it.toString().toLowerCase()
            } else {
                searchIcon.setImageResource(R.drawable.ic_search)
            }
        }
    }

    private fun setupUi() {
        recyclerView.apply {
            adapter = searchResultAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setOnClickListeners() {
        searchIcon.setOnClickListener(this)
        searchResultAdapter.setOnItemClickListener(object :
            SearchResultAdapter.OnItemClickListener {
            override fun onClick(country: Country) {
                val action =
                    SearchFragmentDirections.actionNavigationSearchToNavigationDetails()
                action.country = country
                action.fromSearch = true
                navController.navigate(action)
            }

            override fun onHeartClick(country: Country) {
                if (isFavorite(country)) {
                    db.deleteCountry(country)
                } else {
                    db.addCountry(country)
                }
                countryCodes = db.countryCodes
            }
        })

        sortAreaButton.setOnClickListener(this)
        sortPopulationButton.setOnClickListener(this)
    }

    fun isFavorite(currentCountry: Country): Boolean {
        var flag = false
        for (code in countryCodes) {
            if (currentCountry.flag == code.flag) {
                flag = true
                break
            }
        }
        return flag
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.sort_population -> {
                if (sortOption == SortOptions.Population) {
                    sortOption = SortOptions.None
                    countriesViewModel.clearSortedCountries()
                } else {
                    sortByPopulation()
                }
            }
            R.id.sort_area -> {
                if (sortOption == SortOptions.Area) {
                    sortOption = SortOptions.None
                    countriesViewModel.clearSortedCountries()
                } else {
                    sortByArea()
                }
            }
            R.id.searchIcon -> {
                searchInput.text.clear()
            }
        }
    }

    private fun toggleSortButton(
        buttonToFocus: Button? = null,
        buttonToUnFocus: Button? = null,
        buttonToUnFocus2: Button? = null
    ) {
        buttonToFocus?.setBackgroundResource(R.drawable.filter_bg_filled)
        buttonToFocus?.setTextColor(Color.WHITE)
        buttonToUnFocus?.setBackgroundResource(R.drawable.filter_bg_outline)
        buttonToUnFocus?.setTextColor(resources.getColor(R.color.colorPrimary))
        buttonToUnFocus2?.setBackgroundResource(R.drawable.filter_bg_outline)
        buttonToUnFocus2?.setTextColor(resources.getColor(R.color.colorPrimary))
    }

    private fun sortByPopulation() {
        countriesViewModel.sortCountriesByPopulation()
        sortOption = SortOptions.Population
    }

    private fun sortByArea() {
        countriesViewModel.sortCountriesByArea()
        sortOption = SortOptions.Area
    }

}