package com.nesib.countriesapp.ui.favorites;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nesib.countriesapp.R;
import com.nesib.countriesapp.adapters.FavoritesChildAdapter;
import com.nesib.countriesapp.adapters.FavoritesParentAdapter;
import com.nesib.countriesapp.database.DatabaseHelper;
import com.nesib.countriesapp.models.Country;
import com.nesib.countriesapp.models.FavoriteCountry;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private FavoritesParentAdapter favoritesParentAdapter;
    private FavoritesChildAdapter favoritesChildAdapter;
    private DatabaseHelper db;
    private List<FavoriteCountry> favoriteCountries;
    private List<Country> countryCodes;
    private RecyclerView recyclerView;
    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.parentRecyclerView);
        db = DatabaseHelper.getInstance(getActivity());
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        countryCodes = db.getCountryCodes();
        favoriteCountries = new ArrayList<>();
        List<Country> europe,asia,africa,america,oceania;
        europe = new ArrayList<>();
        asia = new ArrayList<>();
        africa = new ArrayList<>();
        america = new ArrayList<>();
        oceania = new ArrayList<>();

        for(Country code : countryCodes){
            switch (code.getRegion().toLowerCase()){
                case "europe":
                    europe.add(code);
                    break;
                case "asia":
                    asia.add(code);
                    break;
                case "africa":
                    africa.add(code);
                    break;
                case "americas":
                    america.add(code);
                    break;
                case "oceania":
                    oceania.add(code);
                    break;
            }
        }
        if(europe.size()>0){
            FavoriteCountry favoriteCountriesEurope = new FavoriteCountry("europe",europe);
            favoriteCountries.add(favoriteCountriesEurope);
        }
        if(asia.size()>0){
            FavoriteCountry favoriteCountryAsia = new FavoriteCountry("asia",asia);
            favoriteCountries.add(favoriteCountryAsia);
        }
        if(africa.size()>0){
            FavoriteCountry favoriteCountryAfrica = new FavoriteCountry("africa",africa);
            favoriteCountries.add(favoriteCountryAfrica);
        }
        if(america.size()>0){
            FavoriteCountry favoriteCountryAmerica = new FavoriteCountry("americas",america);
            favoriteCountries.add(favoriteCountryAmerica);
        }
        if(oceania.size()>0){
            FavoriteCountry favoriteCountryOceania = new FavoriteCountry("oceania",oceania);
            favoriteCountries.add(favoriteCountryOceania);
        }


        favoritesParentAdapter = new FavoritesParentAdapter(favoriteCountries,navController);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(favoritesParentAdapter);

    }
}