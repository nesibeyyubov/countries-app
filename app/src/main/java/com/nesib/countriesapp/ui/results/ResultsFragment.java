package com.nesib.countriesapp.ui.results;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.nesib.countriesapp.R;
import com.nesib.countriesapp.adapters.SearchResultAdapter;
import com.nesib.countriesapp.database.DatabaseHelper;
import com.nesib.countriesapp.models.Country;
import com.nesib.countriesapp.viewmodels.CountriesViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ResultsFragment extends Fragment implements View.OnClickListener {
    private TextView regionTextView;
    private LinearLayout regionNameContainer;
    private CountriesViewModel countriesViewModel;
    private List<Country> countryList;
    private String regionName;
    private RecyclerView recyclerView;
    private SearchResultAdapter searchResultAdapter;
    private NavController navController;
    private RelativeLayout shimmerLayout;
    private ShimmerFrameLayout shimmerView;
    private DatabaseHelper db;
    private List<Country> countryCodes;
    private EditText searchInput;
    private Button sortPopulationButton, sortAreaButton;
    private List<Country> sortedCountryList;

    private boolean sortedByPopulation = false;
    private boolean sortedByArea = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_results, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        regionTextView = view.findViewById(R.id.regionName);
        regionNameContainer = view.findViewById(R.id.regionNameContainer);
        recyclerView = view.findViewById(R.id.recyclerView);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        shimmerView = view.findViewById(R.id.shimmer_view_container);
        searchInput = view.findViewById(R.id.searchInput);
        sortPopulationButton = view.findViewById(R.id.sort_population);
        sortAreaButton = view.findViewById(R.id.sort_area);

        navController = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        countriesViewModel = new ViewModelProvider(getActivity()).get(CountriesViewModel.class);
        db = DatabaseHelper.getInstance(getActivity());
        setupUi();
        setupRecyclerView();
        if (sortedByPopulation) {
            focusButton(sortPopulationButton);
            searchResultAdapter.setCountryList(sortedCountryList);
        } else if (sortedByArea) {
            focusButton(sortAreaButton);
            searchResultAdapter.setCountryList(sortedCountryList);
        } else {
            fetchData();
        }

        countriesViewModel.getIsLoading().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    shimmerLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
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
                List<Country> filteredCountryList = new ArrayList<>();
                List<Country> listToFilter = countryList;
                if(sortedByArea || sortedByPopulation){
                    listToFilter = sortedCountryList;
                }
                for (Country country : listToFilter) {
                    if (country.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredCountryList.add(country);
                    }
                }
                searchResultAdapter.setCountryList(filteredCountryList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void fetchData() {
        boolean differentRegionSelected = false;
        if (countriesViewModel.getRegionName() != null && !countriesViewModel.getRegionName().equals(regionName.toLowerCase())) {
            differentRegionSelected = true;
        }
        countriesViewModel.setRegionName(regionName.toLowerCase());
        countriesViewModel.getCountries(differentRegionSelected).observe(getActivity(), new Observer<List<Country>>() {
            @Override
            public void onChanged(List<Country> countries) {
                if (countries != null) {
                    searchResultAdapter.setCountryList(countries);
                    countryList = countries;
                }
            }
        });
    }

    public void setupRecyclerView() {
        Log.d("mytag", "setupRecyclerView: ");
        countryCodes = db.getCountryCodes();
        searchResultAdapter = new SearchResultAdapter(countryCodes, getActivity());
        searchResultAdapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Country currentCountry;
                if (sortedByArea || sortedByPopulation) {
                    currentCountry = sortedCountryList.get(position);
                } else {
                    currentCountry = countryList.get(position);
                }
                ResultsFragmentDirections.ActionResultsFragmentToNavigationDetails action =
                        ResultsFragmentDirections.actionResultsFragmentToNavigationDetails();
                action.setCountry(currentCountry);
                navController.navigate(action);
            }

            @Override
            public void onHeartClick(int position) {
                Country currentCountry;
                if (sortedByArea || sortedByPopulation) {
                    currentCountry = sortedCountryList.get(position);
                } else {
                    currentCountry = countryList.get(position);
                }
                if (isFavorite(countryCodes, currentCountry)) {
                    db.deleteCountry(currentCountry);
                } else {
                    db.addCountry(currentCountry);
                }
                countryCodes = db.getCountryCodes();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(searchResultAdapter);
    }

    private void setupUi() {
        Log.d("mytag", "setupUI: ");
        ResultsFragmentArgs args = ResultsFragmentArgs.fromBundle(getArguments());
        regionName = args.getRegionName();
        regionTextView.setText(regionName);
        switch (regionName) {
            case "Europe":
                regionNameContainer.setBackgroundResource(R.drawable.bg_europa);
                break;
            case "Asia":
                regionNameContainer.setBackgroundResource(R.drawable.bg_asia);
                break;
            case "Africa":
                regionNameContainer.setBackgroundResource(R.drawable.bg_africa);
                break;
            case "Americas":
                regionNameContainer.setBackgroundResource(R.drawable.bg_america);
                break;
            case "Oceania":
                regionNameContainer.setBackgroundResource(R.drawable.bg_oceania);
                break;
        }
        sortAreaButton.setOnClickListener(this);
        sortPopulationButton.setOnClickListener(this);
    }

    public boolean isFavorite(List<Country> countryCodes, Country currentCountry) {
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
        Log.d("mytag", "sortByPopulation: ");
        sortedCountryList = new ArrayList<>(countryList);
        Collections.sort(sortedCountryList, new Comparator<Country>() {
            @Override
            public int compare(Country country1, Country country2) {
                if (country1.getPopulation() > country2.getPopulation()) {
                    return -1;
                } else if (country1.getPopulation() < country2.getPopulation()) {
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
                } else if (country1.getArea() < country2.getArea()) {
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