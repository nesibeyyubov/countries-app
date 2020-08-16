package com.nesib.countriesapp.api;

import com.nesib.countriesapp.models.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CountriesApi {
    @GET("region/{region}")
    Call<List<Country>> getCountriesByRegion(@Path("region") String region);

    @GET("name/{name}")
    Call<List<Country>> getCountriesByName(@Path("name") String name);
}
