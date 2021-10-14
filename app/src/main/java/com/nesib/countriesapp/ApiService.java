package com.nesib.countriesapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static Retrofit instance;

    public static Retrofit getInstance(){
        if(instance == null){
            instance = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constants.API_BASE_URL)
                    .build();
        }
        return instance;
    }
}
