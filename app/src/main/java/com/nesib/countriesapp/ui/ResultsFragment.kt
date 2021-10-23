package com.nesib.countriesapp.ui

import android.content.Context
import android.graphics.Color
import com.nesib.countriesapp.database.DatabaseHelper.Companion.getInstance
import com.nesib.countriesapp.models.Country
import androidx.recyclerview.widget.RecyclerView
import com.nesib.countriesapp.adapters.SearchResultAdapter
import androidx.navigation.NavController
import com.nesib.countriesapp.database.DatabaseHelper
import android.os.Bundle
import com.nesib.countriesapp.R
import androidx.lifecycle.ViewModelProvider
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.nesib.countriesapp.utils.DataState
import com.nesib.countriesapp.utils.LifecycleStatus
import com.nesib.countriesapp.utils.SortOptions
import com.nesib.countriesapp.viewmodels.CountriesViewModel

class ResultsFragment : Fragment(R.layout.fragment_results), View.OnClickListener {
    private var lifeCycleStatus: LifecycleStatus? = null

    private lateinit var regionTextView: TextView
    private lateinit var hasFailureText: TextView
    private lateinit var regionNameContainer: LinearLayout
    private lateinit var countriesViewModel: CountriesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchResultAdapter: SearchResultAdapter
    private lateinit var navController: NavController
    private lateinit var shimmerLayout: RelativeLayout
    private lateinit var filterContainer: LinearLayout
    private lateinit var db: DatabaseHelper
    private lateinit var countryCodes: List<Country>
    private lateinit var searchInput: EditText
    private lateinit var sortPopulationButton: Button
    private lateinit var sortAreaButton: Button

    private var mHasFailure = false
    private var sortOption = SortOptions.None


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        regionTextView = view.findViewById(R.id.regionName)
        regionNameContainer = view.findViewById(R.id.regionNameContainer)
        recyclerView = view.findViewById(R.id.recyclerView)
        shimmerLayout = view.findViewById(R.id.shimmerLayout)
        searchInput = view.findViewById(R.id.searchInput)
        sortPopulationButton = view.findViewById(R.id.sort_population)
        sortAreaButton = view.findViewById(R.id.sort_area)
        hasFailureText = view.findViewById(R.id.hasFailureText)
        filterContainer = view.findViewById(R.id.filterContainer)
        navController = Navigation.findNavController(view)


        countriesViewModel = ViewModelProvider(this).get(
            CountriesViewModel::class.java
        )
        db = getInstance(activity)
        setupUi()
        setupRecyclerView()
        if (lifeCycleStatus == LifecycleStatus.OnAttach) {
            countriesViewModel.getCountriesByRegion(
                ResultsFragmentArgs.fromBundle(
                    requireArguments()
                ).regionName)
        }
        subscribeObservers()
        setOnSearchInputChangeListener()
        lifeCycleStatus = LifecycleStatus.OnViewCreated
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifeCycleStatus = LifecycleStatus.OnAttach
    }

    private fun setOnSearchInputChangeListener() {
        searchInput.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                countriesViewModel.sortCountriesBySearchInput(it.toString(), sortOption)
            }
        }
    }

    private fun subscribeObservers() {
        countriesViewModel.countriesByRegion.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    toggleLoading(loading = false)
                    when (sortOption) {
                        SortOptions.None -> {
                            toggleSortButton(
                                buttonToUnFocus = sortPopulationButton,
                                buttonToUnFocus2 = sortAreaButton
                            )
                            searchResultAdapter.setCountryList(it.data!!)
                        }
                        SortOptions.Area -> {
                            countriesViewModel.sortCountriesByArea()
                        }
                        SortOptions.Population -> {
                            countriesViewModel.sortCountriesByPopulation()
                        }
                    }
                }
                is DataState.Loading -> {
                    toggleLoading(loading = true)
                }
                is DataState.Error -> {
                    toggleLoading(loading = false)
                    hasFailureText.visibility = View.VISIBLE
                    filterContainer.visibility = View.GONE
                    hasFailureText.text = it.message
                    mHasFailure = true
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
        countriesViewModel.countriesSortedBySearchInput.observe(viewLifecycleOwner) {
            if (it != null) {
                searchResultAdapter.setCountryList(it)
            }
        }
    }

    private fun toggleLoading(loading: Boolean) {
        shimmerLayout.visibility = if (loading) View.VISIBLE else View.GONE
        recyclerView.visibility = if (loading) View.GONE else View.VISIBLE
    }


    private fun setupRecyclerView() {
        countryCodes = db.countryCodes
        searchResultAdapter = SearchResultAdapter()
        searchResultAdapter.setOnItemClickListener(object :
            SearchResultAdapter.OnItemClickListener {
            override fun onClick(country: Country) {
                val action =
                    ResultsFragmentDirections.actionResultsFragmentToNavigationDetails()
                action.country = country
                navController.navigate(action)
            }

            override fun onHeartClick(country: Country) {
                if (isFavorite(countryCodes, country)) {
                    db.deleteCountry(country)
                } else {
                    db.addCountry(country)
                }
                countryCodes = db.countryCodes
            }
        })
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = searchResultAdapter
    }

    private fun setupUi() {
        assert(arguments != null)
        val args =
            ResultsFragmentArgs.fromBundle(requireArguments())
        regionTextView.text = args.regionName
        when (args.regionName) {
            "Europe" -> regionNameContainer.setBackgroundResource(R.drawable.bg_europa)
            "Asia" -> regionNameContainer.setBackgroundResource(R.drawable.bg_asia)
            "Africa" -> regionNameContainer.setBackgroundResource(R.drawable.bg_africa)
            "Americas" -> regionNameContainer.setBackgroundResource(R.drawable.bg_america)
            "Oceania" -> regionNameContainer.setBackgroundResource(R.drawable.bg_oceania)
        }
        sortAreaButton.setOnClickListener(this)
        sortPopulationButton.setOnClickListener(this)
    }

    fun isFavorite(countryCodes: List<Country>, currentCountry: Country): Boolean {
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
        countriesViewModel.sortCountriesByPopulation(
            searchText = if (searchInput.text.toString()
                    .isEmpty()
            ) null else searchInput.text.toString()
        )
        sortOption = SortOptions.Population
    }

    private fun sortByArea() {
        countriesViewModel.sortCountriesByArea(
            searchText = if (searchInput.text.toString()
                    .isEmpty()
            ) null else searchInput.text.toString()
        )
        sortOption = SortOptions.Area
    }


}