package com.nesib.countriesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Country implements Parcelable {
    private String name;
    private String capital;
    private String region;
    private long population;
    private double area;
    private String flag;
    private String alpha3Code;
    private String[] altSpellings;

    private String[] callingCodes;
    private double[] latlng;
    private String[] borders;
    private Currency[] currencies;
    private Language[] languages;

    public Country() {
    }

    public Country(String name, String capital, String region, long population, double area, String flag, String[] callingCodes, double[] latlng, String[] borders, Currency[] currencies, Language[] languages, String alpha3Code, String[] altSpellings) {
        this.name = name;
        this.capital = capital;
        this.region = region;
        this.population = population;
        this.area = area;
        this.flag = flag;
        this.callingCodes = callingCodes;
        this.latlng = latlng;
        this.borders = borders;
        this.currencies = currencies;
        this.languages = languages;
        this.alpha3Code = alpha3Code;
        this.altSpellings = altSpellings;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", capital='" + capital + '\'' +
                ", region='" + region + '\'' +
                ", population=" + population +
                ", area=" + area +
                ", flag='" + flag + '\'' +
                ", alpha3Code='" + alpha3Code + '\'' +
                ", altSpellings=" + Arrays.toString(altSpellings) +
                ", callingCodes=" + Arrays.toString(callingCodes) +
                ", latlng=" + Arrays.toString(latlng) +
                ", borders=" + Arrays.toString(borders) +
                ", currencies=" + Arrays.toString(currencies) +
                ", languages=" + Arrays.toString(languages) +
                '}';
    }

    protected Country(Parcel in) {
        name = in.readString();
        capital = in.readString();
        region = in.readString();
        population = in.readLong();
        area = in.readDouble();
        flag = in.readString();
        callingCodes = in.createStringArray();
        latlng = in.createDoubleArray();
        borders = in.createStringArray();
        alpha3Code = in.readString();
        altSpellings = in.createStringArray();
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String[] getCallingCodes() {
        return callingCodes;
    }

    public void setCallingCodes(String[] callingCodes) {
        this.callingCodes = callingCodes;
    }

    public double[] getLatlng() {
        return latlng;
    }

    public void setLatlng(double[] latlng) {
        this.latlng = latlng;
    }

    public String[] getBorders() {
        return borders;
    }

    public void setBorders(String[] borders) {
        this.borders = borders;
    }

    public Currency[] getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Currency[] currencies) {
        this.currencies = currencies;
    }

    public Language[] getLanguages() {
        return languages;
    }

    public void setLanguages(Language[] languages) {
        this.languages = languages;
    }

    public String getAlpha3Code() {
        return alpha3Code;
    }

    public void setAlpha3Code(String alpha3Code) {
        this.alpha3Code = alpha3Code;
    }

    public String[] getAltSpellings() {
        return altSpellings;
    }

    public void setAltSpellings(String[] altSpellings) {
        this.altSpellings = altSpellings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(capital);
        parcel.writeString(region);
        parcel.writeLong(population);
        parcel.writeDouble(area);
        parcel.writeString(flag);
        parcel.writeStringArray(callingCodes);
        parcel.writeDoubleArray(latlng);
        parcel.writeStringArray(borders);
        parcel.writeString(alpha3Code);
        parcel.writeStringArray(altSpellings);
    }
}
