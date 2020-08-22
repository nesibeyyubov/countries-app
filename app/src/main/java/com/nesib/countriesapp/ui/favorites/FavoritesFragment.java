package com.nesib.countriesapp.ui.favorites;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
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
import com.nesib.countriesapp.viewmodels.CountriesViewModel;

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
    private TextView noFavorites;
    private CountriesViewModel countriesViewModel;
    private ProgressBar favoritesProgressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.parentRecyclerView);
        favoritesProgressBar = view.findViewById(R.id.favoritesProgressBar);
        noFavorites = view.findViewById(R.id.noFavorites);

        db = DatabaseHelper.getInstance(getActivity());
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        favoritesProgressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new DatabaseReaderAsyncTask(getActivity()).execute();
            }
        }, 200);


    }

    public void setupRecyclerView() {
        favoriteCountries = new ArrayList<>();
        List<Country> europe, asia, africa, america, oceania;
        europe = new ArrayList<>();
        asia = new ArrayList<>();
        africa = new ArrayList<>();
        america = new ArrayList<>();
        oceania = new ArrayList<>();

        for (Country code : countryCodes) {
            switch (code.getRegion().toLowerCase()) {
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
        if (europe.size() > 0) {
            FavoriteCountry favoriteCountriesEurope = new FavoriteCountry("europe", europe);
            favoriteCountries.add(favoriteCountriesEurope);
        }
        if (asia.size() > 0) {
            FavoriteCountry favoriteCountryAsia = new FavoriteCountry("asia", asia);
            favoriteCountries.add(favoriteCountryAsia);
        }
        if (africa.size() > 0) {
            FavoriteCountry favoriteCountryAfrica = new FavoriteCountry("africa", africa);
            favoriteCountries.add(favoriteCountryAfrica);
        }
        if (america.size() > 0) {
            FavoriteCountry favoriteCountryAmerica = new FavoriteCountry("americas", america);
            favoriteCountries.add(favoriteCountryAmerica);
        }
        if (oceania.size() > 0) {
            FavoriteCountry favoriteCountryOceania = new FavoriteCountry("oceania", oceania);
            favoriteCountries.add(favoriteCountryOceania);
        }

        recyclerView.setVisibility(View.VISIBLE);
        favoritesParentAdapter = new FavoritesParentAdapter(favoriteCountries, navController);
        favoritesParentAdapter.setListener(new FavoritesParentAdapter.OnAllItemsDeletedListener() {
            @Override
            public void onAllDeleted() {
                recyclerView.setVisibility(View.GONE);
                noFavorites.setVisibility(View.VISIBLE);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(favoritesParentAdapter);
        if (favoriteCountries.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            noFavorites.setVisibility(View.VISIBLE);
        }
    }

    class DatabaseReaderAsyncTask extends AsyncTask<Void, Void, List<Country>> {
        private DatabaseHelper db;
        private Context context;
        private MutableLiveData<List<Country>> countriesInDatabase;

        public DatabaseReaderAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db = DatabaseHelper.getInstance(context);
        }

        @Override
        protected List<Country> doInBackground(Void... contexts) {
            List<Country> countries = db.getCountryCodes();
            return countries;
        }

        @Override
        protected void onPostExecute(List<Country> countries) {
            super.onPostExecute(countries);
            favoritesProgressBar.setVisibility(View.GONE);
            countryCodes = countries;
            setupRecyclerView();
        }
    }


}