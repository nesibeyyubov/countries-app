package com.nesib.countriesapp.models;

import java.util.List;

public class FavoriteCountry {
    private String regionName;
    private List<Country> countryList;

    public FavoriteCountry(String regionName, List<Country> countryList) {
        this.regionName = regionName;
        this.countryList = countryList;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }
}
