package com.nesib.countriesapp.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nesib.countriesapp.ApiService;
import com.nesib.countriesapp.api.CountriesApi;
import com.nesib.countriesapp.models.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CountriesRepository {
    private static final String TAG = "mytag";
    private static CountriesRepository instance;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<List<Country>> countryList;
    private MutableLiveData<List<Country>> countryListName;
    private MutableLiveData<List<Country>> allCountries;

    public CountriesRepository(){
        countryListName = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        allCountries = new MutableLiveData<>();
    }

    public static CountriesRepository getInstance() {
        if (instance == null) {
            instance = new CountriesRepository();
        }
        return instance;
    }


    public LiveData<List<Country>> getCountriesByRegion(String regionName) {
        countryList = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        isLoading.postValue(true);
        Retrofit apiService = ApiService.getInstance();
        Call<List<Country>> call = apiService.create(CountriesApi.class).getCountriesByRegion(regionName);
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                List<Country> countries = response.body();
                countryList.postValue(countries);
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.d("mytag", "onFailure: " + t.getMessage());
                isLoading.postValue(false);
            }
        });

        return countryList;
    }


    public LiveData<List<Country>> getCountriesByName(String name) {

        if(name != null){
            isLoading.postValue(true);
            Retrofit apiService = ApiService.getInstance();
            Call<List<Country>> call = apiService.create(CountriesApi.class).getCountriesByName(name);
            call.enqueue(new Callback<List<Country>>() {
                @Override
                public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                    isLoading.postValue(false);
                    Log.d(TAG, "onResponse: ");
                    countryListName.postValue(response.body());
                }

                @Override
                public void onFailure(Call<List<Country>> call, Throwable t) {
                    isLoading.postValue(false);
                }
            });
        }
        return countryListName;
    }


    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }


    public LiveData<List<Country>> getAllCountries() {
        isLoading.postValue(true);
        Call<List<Country>> call = ApiService.getInstance().create(CountriesApi.class).getAllCountries();
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if(response.body() != null){
                    allCountries.postValue(response.body());
                }
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                isLoading.postValue(false);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });

        return allCountries;
    }
}
