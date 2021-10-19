package com.nesib.countriesapp.ui.results

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
import android.text.TextWatcher
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.nesib.countriesapp.DataState
import com.nesib.countriesapp.LifecycleStatus
import com.nesib.countriesapp.viewmodels.CountriesViewModel2
import java.util.*

class ResultsFragment : Fragment(R.layout.fragment_results), View.OnClickListener {
    private var lifeCycleStatus:LifecycleStatus? = null

    private lateinit var regionTextView: TextView
    private lateinit var hasFailureText: TextView
    private lateinit var regionNameContainer: LinearLayout
    private lateinit var countriesViewModel: CountriesViewModel2
    private lateinit var countryList: List<Country>
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

    private var sortedCountryList: List<Country>? = null
    private var filteredCountryList: MutableList<Country>? = null
    private var sortedByPopulation = false
    private var sortedByArea = false
    private var filteredByName = false
    private var mHasFailure = false


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
            CountriesViewModel2::class.java
        )
        db = getInstance(activity)
        setupUi()
        setupRecyclerView()
        if(lifeCycleStatus == LifecycleStatus.OnAttach){
            countriesViewModel.getCountriesByRegion(ResultsFragmentArgs.fromBundle(requireArguments()).regionName)
        }
        subscribeObservers()
        setOnSearchInputChangeListener()
        lifeCycleStatus = LifecycleStatus.OnViewCreated

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifeCycleStatus = LifecycleStatus.OnAttach
    }

    private fun setOnSearchInputChangeListener(){
        searchInput.addTextChangedListener {
            if (mHasFailure) {
                return@addTextChangedListener
            }
            if (it.toString().isEmpty()) {
                filteredByName = false
                if (sortedByPopulation) {
                    sortByPopulation()
                } else if (sortedByArea) {
                    sortByArea()
                }
                return@addTextChangedListener
            }
            filteredCountryList = ArrayList()
            var listToFilter = countryList
            if (sortedByArea || sortedByPopulation) {
                listToFilter = sortedCountryList!!
            }
            for (country in listToFilter) {
                if (country.name!!.toLowerCase()
                        .contains(it.toString().toLowerCase())
                ) {
                    (filteredCountryList as ArrayList<Country>).add(country)
                }
            }
            filteredByName = true
            searchResultAdapter.setCountryList(filteredCountryList as ArrayList<Country>)
        }
    }

    private fun subscribeObservers(){
        countriesViewModel.countriesByRegion.observe(viewLifecycleOwner){
            when(it){
                is DataState.Success -> {
                    toggleLoading(loading = false)
                    searchResultAdapter.setCountryList(it.data!!)
                    countryList = it.data
                }
                is DataState.Loading ->{
                    toggleLoading(loading = true)
                }
                is DataState.Error ->{
                    toggleLoading(loading = false)
                    hasFailureText.visibility = View.VISIBLE
                    filterContainer.visibility = View.GONE
                    hasFailureText.text = it.message
                    mHasFailure = true
                }
            }
        }
    }

    private fun toggleLoading(loading:Boolean){
        shimmerLayout.visibility = if(loading) View.VISIBLE else View.GONE
        recyclerView.visibility = if(loading) View.GONE else View.VISIBLE
    }


    private fun setupRecyclerView() {
        countryCodes = db.countryCodes
        searchResultAdapter = SearchResultAdapter(countryCodes, requireActivity())
        searchResultAdapter.setOnItemClickListener(object :
            SearchResultAdapter.OnItemClickListener {
            override fun onClick(position: Int) {
                val currentCountry: Country = if (sortedByArea || sortedByPopulation) {
                    sortedCountryList!![position]
                } else if (filteredByName) {
                    filteredCountryList!![position]
                } else {
                    countryList[position]
                }
                val action = ResultsFragmentDirections.actionResultsFragmentToNavigationDetails()
                action.setCountry(currentCountry)
                navController.navigate(action)
            }

            override fun onHeartClick(position: Int) {
                val currentCountry: Country
                currentCountry = if (sortedByArea || sortedByPopulation) {
                    sortedCountryList!![position]
                } else if (filteredByName) {
                    filteredCountryList!![position]
                } else {
                    countryList[position]
                }
                if (isFavorite(countryCodes, currentCountry)) {
                    db.deleteCountry(currentCountry)
                } else {
                    db.addCountry(currentCountry)
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
        val args = ResultsFragmentArgs.fromBundle(requireArguments())
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
                if (!sortedByPopulation) {
                    focusButton(sortPopulationButton)
                    sortByPopulation()
                    if (sortedByArea) {
                        unFocusButton(sortAreaButton)
                        sortedByArea = false
                    }
                } else {
                    unFocusButton(sortPopulationButton)
                    searchResultAdapter.setCountryList(countryList)
                }
                sortedByPopulation = !sortedByPopulation
            }
            R.id.sort_area -> {
                if (!sortedByArea) {
                    focusButton(sortAreaButton)
                    sortByArea()
                    if (sortedByPopulation) {
                        unFocusButton(sortPopulationButton)
                        sortedByPopulation = false
                    }
                } else {
                    unFocusButton(sortAreaButton)
                    searchResultAdapter.setCountryList(countryList)
                }
                sortedByArea = !sortedByArea
            }
        }
    }

    private fun sortByPopulation() {
        sortedCountryList = if (filteredByName) {
            ArrayList(filteredCountryList)
        } else {
            ArrayList(countryList)
        }
        Collections.sort(sortedCountryList, Comparator { country1, country2 ->
            if (country1.population > country2.population) {
                return@Comparator -1
            } else if (country1.population < country2.population) {
                return@Comparator 1
            }
            0
        })
        searchResultAdapter.setCountryList(sortedCountryList as ArrayList<Country>)
    }

    private fun sortByArea() {
        sortedCountryList = if (filteredByName) {
            ArrayList(filteredCountryList)
        } else {
            ArrayList(countryList)
        }
        Collections.sort(sortedCountryList, Comparator { country1, country2 ->
            if (country1.area > country2.area) {
                return@Comparator -1
            } else if (country1.area < country2.area) {
                return@Comparator 1
            }
            0
        })
        searchResultAdapter.setCountryList(sortedCountryList as ArrayList<Country>)
    }

    private fun focusButton(buttonToFocus: Button?) {
        buttonToFocus!!.setBackgroundResource(R.drawable.filter_bg_filled)
        buttonToFocus.setTextColor(Color.WHITE)
    }

    private fun unFocusButton(buttonToUnFocus: Button?) {
        buttonToUnFocus!!.setBackgroundResource(R.drawable.filter_bg_outline)
        buttonToUnFocus.setTextColor(resources.getColor(R.color.colorPrimary))
    }
}