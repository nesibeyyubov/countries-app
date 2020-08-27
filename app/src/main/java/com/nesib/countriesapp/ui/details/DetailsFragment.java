package com.nesib.countriesapp.ui.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.nesib.countriesapp.R;
import com.nesib.countriesapp.adapters.CountryBordersAdapter;
import com.nesib.countriesapp.database.DatabaseHelper;
import com.nesib.countriesapp.models.Country;
import com.nesib.countriesapp.models.Currency;
import com.nesib.countriesapp.models.Language;
import com.nesib.countriesapp.viewmodels.CountriesViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment implements View.OnClickListener {
    private Country country;
    private TextView countryName, noBorders, countryFullName, currency, regionName, capitalName, population, area, language, callingCodes;
    private ImageView flagImage;
    private ImageButton backButton, heartButton;
    private RecyclerView bordersRecyclerView;
    private DatabaseHelper db;
    private List<Country> countryCodes;
    private ProgressBar bordersProgressBar;
    private LinearLayout showMapButton;
    private NavController navController;
    private NestedScrollView nestedScrollView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_country_details, container, false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flagImage = view.findViewById(R.id.flagImage);
        countryName = view.findViewById(R.id.countryName);
        countryFullName = view.findViewById(R.id.countryFullName);
        currency = view.findViewById(R.id.currency);
        heartButton = view.findViewById(R.id.heartButton);
        capitalName = view.findViewById(R.id.capitalName);
        population = view.findViewById(R.id.population);
        area = view.findViewById(R.id.area);
        language = view.findViewById(R.id.language);
        callingCodes = view.findViewById(R.id.callingCode);
        backButton = view.findViewById(R.id.backButton);
        noBorders = view.findViewById(R.id.noBorders);
        bordersProgressBar = view.findViewById(R.id.bordersProgressBar);
        bordersRecyclerView = view.findViewById(R.id.bordersRecyclerView);
        showMapButton = view.findViewById(R.id.showMapButton);
        regionName = view.findViewById(R.id.regionName);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);

        navController = Navigation.findNavController(view);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CountriesViewModel countriesViewModel = new ViewModelProvider(getActivity()).get(CountriesViewModel.class);
        db = DatabaseHelper.getInstance(getActivity());
        if (getArguments() != null) {
            country = DetailsFragmentArgs.fromBundle(getArguments()).getCountry();
            setupUi();
            backButton.setOnClickListener(this);
            heartButton.setOnClickListener(this);
            showMapButton.setOnClickListener(this);
        }
    }

    public void setupUi() {
        countryCodes = db.getCountryCodes();
        for (Country code : countryCodes) {
            if (code.getFlag().equals(country.getFlag())) {
                heartButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorites_filled, null));
            }
        }
        GlideToVectorYou
                .init()
                .with(getActivity())
                .load(Uri.parse(country.getFlag()), flagImage);
        countryName.setText(country.getName());
        if (country.getCapital() == null || country.getCapital().length() == 0) {
            capitalName.setText(getString(R.string.capital_text) + "  " + getString(R.string.no_information_text));
        } else {
            capitalName.setText(getString(R.string.capital_text) + "  " + country.getCapital());
        }
        population.setText("" + country.getPopulation());
        flagImage.setTransitionName(country.getFlag());

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.###");
        population.setText(decimalFormat.format(country.getPopulation()));

        String areaText = Double.toString(country.getArea());
        areaText = areaText.substring(0, areaText.length() - 2);
        area.setText(areaText);

        String[] callingCodesArray = country.getCallingCodes();
        double[] latlngArray = country.getLatlng();
        String[] bordersArray = country.getBorders();
        Currency[] currenciesArray = country.getCurrencies();
        Language[] languagesArray = country.getLanguages();
        String[] countryFullNamesArray = country.getAltSpellings();
        StringBuilder borderText = new StringBuilder();
        StringBuilder languageText = new StringBuilder();
        for (int i = 0; i < languagesArray.length; i++) {
            languageText.append(languagesArray[i].getName()).append(",");
        }
        languageText.deleteCharAt(languageText.length() - 1);

        if (callingCodesArray[0] != null && callingCodesArray[0].length() > 0) {
            callingCodes.setText("+" + callingCodesArray[0]);
        } else {
            callingCodes.setText(getString(R.string.no_information_text));
        }

        // Sometimes there can be only 1 full name in rest api response
        if (countryFullNamesArray.length > 1) {
            countryFullName.setText(countryFullNamesArray[1]);
        } else {
            countryFullName.setText(country.getName());
        }
        language.setText(languageText);
        if (currenciesArray.length > 0) {
            currency.setText(currenciesArray[0].getName());
        }
        for (int i = 0; i < bordersArray.length; i++) {
            borderText.append(bordersArray[i]).append(",");
        }
        if (borderText.length() > 0) {
            borderText.deleteCharAt(borderText.length() - 1);
        } else {
            borderText.append(getString(R.string.no_borders_text));
        }
        if(country.getRegion().isEmpty()){
            regionName.setText(R.string.no_information_text);
        }
        else{
            regionName.setText(country.getRegion());
        }
        setupCountryBorders(bordersArray);
    }


    public void setupCountryBorders(String[] bordersArray) {
        List<Country> borderCountries = new ArrayList<>();
        CountriesViewModel countriesViewModel = new ViewModelProvider(getActivity()).get(CountriesViewModel.class);
        assert getArguments() != null;
        if (DetailsFragmentArgs.fromBundle(getArguments()).getFromSearch()) {
            countriesViewModel.setRegionName(country.getRegion());
            if (country.getRegion().isEmpty()) {
                countriesViewModel.setRegionName("oceania");
            }
            bordersProgressBar.setVisibility(View.VISIBLE);
        }

        countriesViewModel.getAllCountries().observe(getViewLifecycleOwner(), new Observer<List<Country>>() {
            @Override
            public void onChanged(List<Country> countryList) {
                bordersProgressBar.setVisibility(View.GONE);
                for (Country country : countryList) {
                    for (String border : bordersArray) {
                        if (country.getAlpha3Code().equals(border)) {
                            borderCountries.add(country);
                            break;
                        }
                    }
                }
                if (borderCountries.size() == 0) {
                    noBorders.setVisibility(View.VISIBLE);
                    noBorders.setText(R.string.no_borders_text);
                }
                CountryBordersAdapter adapter = new CountryBordersAdapter(borderCountries);
                bordersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                bordersRecyclerView.setHasFixedSize(true);
                bordersRecyclerView.setAdapter(adapter);

                adapter.setListener(new CountryBordersAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Country selectedCountry) {
                        country = selectedCountry;
                        nestedScrollView.fullScroll(View.FOCUS_UP);
                        setupUi();

                    }
                });

            }
        });

        countriesViewModel.getHasFailure().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean hasFailure) {
                if (hasFailure) {
                    bordersProgressBar.setVisibility(View.GONE);
                    noBorders.setVisibility(View.VISIBLE);
                    noBorders.setText(getString(R.string.something_went_wrong));
                    noBorders.setTextColor(getResources().getColor(R.color.colorRed));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                navController.popBackStack();
                break;
            case R.id.heartButton:
                if (isFavorite()) {
                    db.deleteCountry(country);
                    heartButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorites_outline, null));
                } else {
                    db.addCountry(country);
                    heartButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorites_filled, null));
                }
                break;
            case R.id.showMapButton:
                DetailsFragmentDirections.ActionNavigationDetailsToMapFragment action =
                        DetailsFragmentDirections.actionNavigationDetailsToMapFragment();
                double[] latlng = country.getLatlng();
                float lat = (float) latlng[0];
                float lng = (float) latlng[1];
                float[] latLngArgument = new float[]{lat, lng};
                action.setLatLng(latLngArgument);
                navController.navigate(action);
                break;
        }
    }


    public boolean isFavorite() {
        countryCodes = db.getCountryCodes();
        boolean flag = false;
        for (Country code : countryCodes) {
            if (country.getFlag().equals(code.getFlag())) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}