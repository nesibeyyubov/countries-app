package com.nesib.countriesapp.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nesib.countriesapp.models.Country;
import com.nesib.countriesapp.repositories.CountriesRepository;

import java.util.List;

public class CountriesViewModel extends ViewModel {
    private CountriesRepository repository;
    private LiveData<Boolean> isLoading;
    private LiveData<List<Country>> countryList;
    private LiveData<List<Country>> countryListName;
    private LiveData<List<Country>> allCountries;
    private LiveData<Boolean> hasFailure;
    private String regionName;

    public LiveData<Boolean> getHasFailure() {
        hasFailure = repository.getHasFailure();
        return hasFailure;
    }

    public CountriesViewModel(){
        repository = CountriesRepository.getInstance();
    }

    public void setRegionName(String regionName){
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }

    public LiveData<List<Country>> getCountries(boolean differentRegionSelected){
        if(countryList == null || differentRegionSelected){
            isLoading = new MutableLiveData<>();
            countryList = repository.getCountriesByRegion(regionName);
            isLoading = repository.getIsLoading();
        }
        return countryList;
    }

    public LiveData<Boolean> getIsLoading() {
        if(isLoading == null){
            isLoading = repository.getIsLoading();
        }
        return isLoading;
    }

    public LiveData<List<Country>> getCountryListName(){
        if(countryListName == null){
            countryListName = repository.getCountriesByName(null);
        }
        return countryListName;
    }

    public void fetchCountriesByName(String name){
        repository.getCountriesByName(name);
    }

    public LiveData<List<Country>> getAllCountries(){
        if(allCountries == null){
            allCountries = repository.getAllCountries();
        }
        return allCountries;
    }
}
