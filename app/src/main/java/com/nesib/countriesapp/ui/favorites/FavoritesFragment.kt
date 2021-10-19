package com.nesib.countriesapp.ui.favorites

import android.content.Context
import com.nesib.countriesapp.adapters.FavoritesParentAdapter
import com.nesib.countriesapp.adapters.FavoritesChildAdapter
import com.nesib.countriesapp.database.DatabaseHelper
import com.nesib.countriesapp.models.FavoriteCountry
import com.nesib.countriesapp.models.Country
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.NavController
import android.widget.TextView
import android.widget.ProgressBar
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.nesib.countriesapp.R
import com.nesib.countriesapp.adapters.FavoritesParentAdapter.OnAllItemsDeletedListener
import androidx.recyclerview.widget.LinearLayoutManager
import android.os.AsyncTask
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.nesib.countriesapp.viewmodels.CountriesViewModel2

class FavoritesFragment : Fragment() {
    private var favoritesParentAdapter: FavoritesParentAdapter? = null
    private val favoritesChildAdapter: FavoritesChildAdapter? = null
    private var db: DatabaseHelper? = null
    private var favoriteCountries = mutableListOf<FavoriteCountry>()
    private var countryCodes: List<Country>? = null
    private var recyclerView: RecyclerView? = null
    private var navController: NavController? = null
    private var noFavorites: TextView? = null
    private val countriesViewModel: CountriesViewModel2? = null
    private var favoritesProgressBar: ProgressBar? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.parentRecyclerView)
        favoritesProgressBar = view.findViewById(R.id.favoritesProgressBar)
        noFavorites = view.findViewById(R.id.noFavorites)
        db = DatabaseHelper.getInstance(activity)
        navController = Navigation.findNavController(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favoritesProgressBar!!.visibility = View.VISIBLE
        Handler().postDelayed({ DatabaseReaderAsyncTask(activity).execute() }, 200)
    }

    fun setupRecyclerView() {
        val europe = mutableListOf<Country>()
        val asia = mutableListOf<Country>()
        val africa = mutableListOf<Country>()
        val america = mutableListOf<Country>()
        val oceania = mutableListOf<Country>()
        for (code in countryCodes!!) {
            when (code.region?.toLowerCase()) {
                "europe" -> europe.add(code)
                "asia" -> asia.add(code)
                "africa" -> africa.add(code)
                "americas" -> america.add(code)
                "oceania" -> oceania.add(code)
            }
        }
        if (europe.size > 0) {
            val favoriteCountriesEurope = FavoriteCountry("europe", europe)
            favoriteCountries.add(favoriteCountriesEurope)
        }
        if (asia.size > 0) {
            val favoriteCountryAsia = FavoriteCountry("asia", asia)
            favoriteCountries.add(favoriteCountryAsia)
        }
        if (africa.size > 0) {
            val favoriteCountryAfrica = FavoriteCountry("africa", africa)
            favoriteCountries.add(favoriteCountryAfrica)
        }
        if (america.size > 0) {
            val favoriteCountryAmerica = FavoriteCountry("americas", america)
            favoriteCountries.add(favoriteCountryAmerica)
        }
        if (oceania.size > 0) {
            val favoriteCountryOceania = FavoriteCountry("oceania", oceania)
            favoriteCountries.add(favoriteCountryOceania)
        }
        recyclerView!!.visibility = View.VISIBLE
        favoritesParentAdapter = FavoritesParentAdapter(favoriteCountries, navController!!)
        favoritesParentAdapter!!.setListener(object : OnAllItemsDeletedListener {
            override fun onAllDeleted() {
                recyclerView!!.visibility = View.GONE
                noFavorites!!.visibility = View.VISIBLE
            }
        })
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.adapter = favoritesParentAdapter
        if (favoriteCountries.size == 0) {
            recyclerView!!.visibility = View.GONE
            noFavorites!!.visibility = View.VISIBLE
        }
    }

    internal inner class DatabaseReaderAsyncTask(private val context: Context?) :
        AsyncTask<Void?, Void?, List<Country>>() {
        private var db: DatabaseHelper? = null
        private val countriesInDatabase: MutableLiveData<List<Country>>? = null
        override fun onPreExecute() {
            super.onPreExecute()
            db = DatabaseHelper.getInstance(context)
        }

        override fun doInBackground(vararg p0: Void?): List<Country>? {
            return db!!.countryCodes
        }

        override fun onPostExecute(countries: List<Country>) {
            super.onPostExecute(countries)
            favoritesProgressBar!!.visibility = View.GONE
            countryCodes = countries
            setupRecyclerView()
        }
    }
}