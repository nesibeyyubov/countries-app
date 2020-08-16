package com.nesib.countriesapp.ui.search;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.nesib.countriesapp.ApiService;
import com.nesib.countriesapp.R;
import com.nesib.countriesapp.adapters.SearchResultAdapter;
import com.nesib.countriesapp.api.CountriesApi;
import com.nesib.countriesapp.database.DatabaseHelper;
import com.nesib.countriesapp.models.Country;
import com.nesib.countriesapp.viewmodels.CountriesViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchFragment extends Fragment implements View.OnClickListener {
    private List<Country> countryList;
    private RecyclerView recyclerView;
    private EditText searchInput;
    private ShimmerFrameLayout shimmerView;
    private RelativeLayout shimmerLayout;
    private NavController navController;
    private TextView countryNotFound, resultsTextView;
    private SearchResultAdapter searchResultAdapter;
    private DatabaseHelper db;
    private List<Country> countryCodes;
    private Button sortPopulationButton, sortAreaButton;
    private LinearLayout filterContainer;

    private boolean sortedByPopulation = false;
    private boolean sortedByArea = false;

    private List<Country> sortedCountryList;
    private CountriesViewModel countriesViewModel;
    private String searchText = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchInput = view.findViewById(R.id.searchInput);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        shimmerView = view.findViewById(R.id.shimmer_view_container);
        countryNotFound = view.findViewById(R.id.countryNotFound);
        resultsTextView = view.findViewById(R.id.resultsTextView);
        sortPopulationButton = view.findViewById(R.id.sort_population);
        sortAreaButton = view.findViewById(R.id.sort_area);
        filterContainer = view.findViewById(R.id.filterContainer);

        navController = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = DatabaseHelper.getInstance(getActivity());
        countryCodes = db.getCountryCodes();
        searchResultAdapter = new SearchResultAdapter(countryCodes,getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(searchResultAdapter);
        countryNotFound.setText("Please search for a country");
        countryNotFound.setVisibility(View.VISIBLE);

        if (sortedByPopulation) {
            focusButton(sortPopulationButton);
            setupUi();
            searchResultAdapter.setCountryList(sortedCountryList);
        } else if (sortedByArea) {
            focusButton(sortAreaButton);
            setupUi();
            searchResultAdapter.setCountryList(sortedCountryList);
        } else {
            fetchData();
        }



        searchResultAdapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Country currentCountry;
                SearchFragmentDirections.ActionNavigationSearchToNavigationDetails action =
                        SearchFragmentDirections.actionNavigationSearchToNavigationDetails();
                if(sortedByArea || sortedByPopulation){
                    currentCountry = sortedCountryList.get(position);
                }
                else{
                    currentCountry = countryList.get(position);
                }
                action.setCountry(currentCountry);
                action.setFromSearch(true);
                navController.navigate(action);
            }

            @Override
            public void onHeartClick(int position) {
                Country currentCountry = countryList.get(position);
                if(sortedByPopulation || sortedByArea){
                    currentCountry = sortedCountryList.get(position);
                }
                if (isFavorite(currentCountry)) {
                    db.deleteCountry(currentCountry);
                } else {
                    db.addCountry(currentCountry);
                }
                countryCodes = db.getCountryCodes();
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    return;
                }
                if(charSequence.toString().equals(searchText)){
                    return;
                }
                searchText = charSequence.toString();
                countryNotFound.setVisibility(View.GONE);
                shimmerLayout.setVisibility(View.VISIBLE);
                shimmerView.startShimmer();
                countriesViewModel.fetchCountriesByName(searchText);
//               @Override
//                   public void onFailure(Call<List<Country>> call, Throwable t) {
//                        shimmerLayout.setVisibility(View.GONE);
//                        shimmerView.stopShimmer();
//                        Log.d("mytag", "onFailure: "+t.getMessage());
//                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("mytag", "afterTextChanged: ");
            }
        });
    }
    public void fetchData(){
        countriesViewModel = new ViewModelProvider(getActivity()).get(CountriesViewModel.class);
        countriesViewModel.getCountryListName().observe(getActivity(), new Observer<List<Country>>() {
            @Override
            public void onChanged(List<Country> countriesResponse) {
                countryList = countriesResponse;
                setupUi();
            }
        });
    }

    public void setupUi() {
        shimmerLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        filterContainer.setVisibility(View.VISIBLE);
        shimmerView.stopShimmer();
        if (countryList != null) {
            countryNotFound.setVisibility(View.GONE);
            resultsTextView.setVisibility(View.VISIBLE);
            if(sortedByPopulation){
                sortByPopulation();
            }
            else if(sortedByArea){
                sortByArea();
            }
            else{
                searchResultAdapter.setCountryList(countryList);
            }

        } else {
            countryNotFound.setVisibility(View.VISIBLE);
            countryNotFound.setText("Country not found!");
            resultsTextView.setVisibility(View.VISIBLE);
            searchResultAdapter.setCountryList(new ArrayList<>());
            countryList = new ArrayList<>();
        }
        sortAreaButton.setOnClickListener(this);
        sortPopulationButton.setOnClickListener(this);
    }

    public boolean isFavorite(Country currentCountry) {
        boolean flag = false;
        for (Country code : countryCodes) {
            if (currentCountry.getFlag().equals(code.getFlag())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sort_population:
                if (!sortedByPopulation) {
                    focusButton(sortPopulationButton);
                    sortByPopulation();
                    if (sortedByArea) {
                        unFocusButton(sortAreaButton);
                        sortedByArea = false;
                    }
                } else {
                    unFocusButton(sortPopulationButton);
                    searchResultAdapter.setCountryList(countryList);
                }
                sortedByPopulation = !sortedByPopulation;
                break;
            case R.id.sort_area:
                if (!sortedByArea) {
                    focusButton(sortAreaButton);
                    sortByArea();
                    if (sortedByPopulation) {
                        unFocusButton(sortPopulationButton);
                        sortedByPopulation = false;
                    }
                } else {
                    unFocusButton(sortAreaButton);
                    searchResultAdapter.setCountryList(countryList);
                }
                sortedByArea = !sortedByArea;
                break;
        }
    }

    public void sortByPopulation() {
        sortedCountryList = new ArrayList<>(countryList);
        Collections.sort(sortedCountryList, new Comparator<Country>() {
            @Override
            public int compare(Country country1, Country country2) {
                if (country1.getPopulation() > country2.getPopulation()) {
                    return -1;
                }
                if (country1.getPopulation() < country2.getPopulation()) {
                    return 1;
                }
                return 0;
            }
        });
        searchResultAdapter.setCountryList(sortedCountryList);
    }

    public void sortByArea() {
        sortedCountryList = new ArrayList<>(countryList);
        Collections.sort(sortedCountryList, new Comparator<Country>() {
            @Override
            public int compare(Country country1, Country country2) {
                if (country1.getArea() > country2.getArea()) {
                    return -1;
                }
                if (country1.getArea() < country2.getArea()) {
                    return 1;
                }
                return 0;
            }
        });
        searchResultAdapter.setCountryList(sortedCountryList);
    }

    public void focusButton(Button buttonToFocus) {
        buttonToFocus.setBackgroundResource(R.drawable.filter_bg_filled);
        buttonToFocus.setTextColor(Color.WHITE);
    }

    public void unFocusButton(Button buttonToUnfocus) {
        buttonToUnfocus.setBackgroundResource(R.drawable.filter_bg_outline);
        buttonToUnfocus.setTextColor(getResources().getColor(R.color.colorPrimary));
    }


}