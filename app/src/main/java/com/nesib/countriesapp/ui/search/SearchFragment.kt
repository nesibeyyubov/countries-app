package com.nesib.countriesapp.ui.search

import android.graphics.Color
import com.nesib.countriesapp.database.DatabaseHelper.Companion.getInstance
import com.nesib.countriesapp.models.Country
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import androidx.navigation.NavController
import com.nesib.countriesapp.adapters.SearchResultAdapter
import com.nesib.countriesapp.database.DatabaseHelper
import android.os.Bundle
import com.nesib.countriesapp.R
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.TextWatcher
import android.text.Editable
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.nesib.countriesapp.viewmodels.CountriesViewModel2
import java.util.*

class SearchFragment : Fragment(R.layout.fragment_search), View.OnClickListener {
    private lateinit var countryList: List<Country>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchInput: EditText
    private lateinit var shimmerView: ShimmerFrameLayout
    private lateinit var shimmerLayout: RelativeLayout
    private lateinit var navController: NavController
    private lateinit var countryNotFound: TextView
    private lateinit var resultsTextView: TextView
    private lateinit var searchResultAdapter: SearchResultAdapter
    private lateinit var db: DatabaseHelper
    private lateinit var countryCodes: List<Country>
    private lateinit var sortPopulationButton: Button
    private lateinit var sortAreaButton: Button
    private lateinit var filterContainer: LinearLayout

    private var sortedByPopulation = false
    private var sortedByArea = false
    private var sortedCountryList: List<Country>? = null
    private lateinit var countriesViewModel: CountriesViewModel2
    private var searchText = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        searchInput = view.findViewById(R.id.searchInput)
        shimmerLayout = view.findViewById(R.id.shimmerLayout)
        shimmerView = view.findViewById(R.id.shimmer_view_container)
        countryNotFound = view.findViewById(R.id.countryNotFound)
        resultsTextView = view.findViewById(R.id.resultsTextView)
        sortPopulationButton = view.findViewById(R.id.sort_population)
        sortAreaButton = view.findViewById(R.id.sort_area)
        filterContainer = view.findViewById(R.id.filterContainer)
        navController = Navigation.findNavController(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        db = getInstance(requireActivity())
//        countryCodes = db.countryCodes
//        searchResultAdapter = SearchResultAdapter(countryCodes, requireActivity())
//        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
//        recyclerView.adapter = searchResultAdapter
//        countryNotFound.text = getString(R.string.search_for_country_text)
//        countryNotFound.visibility = View.VISIBLE
//        if (sortedByPopulation) {
//            focusButton(sortPopulationButton)
//            setupUi()
//            searchResultAdapter.setCountryList(sortedCountryList!!)
//        } else if (sortedByArea) {
//            focusButton(sortAreaButton)
//            setupUi()
//            searchResultAdapter.setCountryList(sortedCountryList!!)
//        } else {
//            fetchData()
//        }
//        countriesViewModel.hasFailure.observe(viewLifecycleOwner, { hasFailure ->
//            if (hasFailure) {
//                countryNotFound.visibility = View.VISIBLE
//                countryNotFound.setTextColor(resources.getColor(R.color.colorRed))
//                countryNotFound.text = getString(R.string.check_internet_error_text)
//                filterContainer.visibility = View.GONE
//                shimmerLayout.visibility = View.GONE
//            }
//        })
//        searchResultAdapter.setOnItemClickListener(object :
//            SearchResultAdapter.OnItemClickListener {
//            override fun onClick(position: Int) {
//                val action = SearchFragmentDirections.actionNavigationSearchToNavigationDetails()
//                val currentCountry = if (sortedByArea || sortedByPopulation) {
//                    sortedCountryList!![position]
//                } else {
//                    countryList[position]
//                }
//                action.setCountry(currentCountry)
//                action.fromSearch = true
//                navController.navigate(action)
//            }
//
//            override fun onHeartClick(position: Int) {
//                var currentCountry = countryList[position]
//                if (sortedByPopulation || sortedByArea) {
//                    currentCountry = sortedCountryList!![position]
//                }
//                if (isFavorite(currentCountry)) {
//                    db.deleteCountry(currentCountry)
//                } else {
//                    db.addCountry(currentCountry)
//                }
//                countryCodes = db.countryCodes
//            }
//        })
//        searchInput!!.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
//            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//                if (charSequence.isEmpty()) {
//                    return
//                }
//                if (charSequence.toString() == searchText) {
//                    return
//                }
//                searchText = charSequence.toString()
//                countryNotFound.visibility = View.GONE
//                shimmerLayout.visibility = View.VISIBLE
//                shimmerView.startShimmer()
//                countriesViewModel.fetchCountriesByName(searchText)
//            }
//
//            override fun afterTextChanged(editable: Editable) {}
//        })
    }

//    private fun fetchData() {
//        countriesViewModel = ViewModelProvider(this).get(CountriesViewModel::class.java)
//        countriesViewModel.countryListName.observe(viewLifecycleOwner, { countriesResponse ->
//            countryList = countriesResponse
//            setupUi()
//        })
//    }

    private fun setupUi() {
        shimmerLayout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        filterContainer.visibility = View.VISIBLE
        shimmerView.stopShimmer()
        if (countryList != null) {
            countryNotFound.visibility = View.GONE
            resultsTextView.visibility = View.VISIBLE
            if (sortedByPopulation) {
                sortByPopulation()
            } else if (sortedByArea) {
                sortByArea()
            } else {
                searchResultAdapter.setCountryList(countryList)
            }
        } else {
            countryNotFound.visibility = View.VISIBLE
            countryNotFound.text = getString(R.string.country_not_found_text)
            resultsTextView.visibility = View.VISIBLE
            searchResultAdapter.setCountryList(ArrayList())
            countryList = ArrayList()
        }
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
        sortedCountryList = ArrayList(countryList)
        Collections.sort(sortedCountryList, Comparator { country1, country2 ->
            if (country1.population > country2.population) {
                return@Comparator -1
            }
            if (country1.population < country2.population) {
                1
            } else 0
        })
        searchResultAdapter.setCountryList(sortedCountryList as ArrayList<Country>)
    }

    private fun sortByArea() {
        sortedCountryList = ArrayList(countryList)
        Collections.sort(sortedCountryList, Comparator { country1, country2 ->
            if (country1.area > country2.area) {
                return@Comparator -1
            }
            if (country1.area < country2.area) {
                1
            } else 0
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