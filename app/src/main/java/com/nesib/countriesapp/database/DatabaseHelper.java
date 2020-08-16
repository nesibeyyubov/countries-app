package com.nesib.countriesapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nesib.countriesapp.Constants;
import com.nesib.countriesapp.models.Country;
import com.nesib.countriesapp.models.Currency;
import com.nesib.countriesapp.models.Language;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, Constants.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREATE TABLE countries_table(id,flag,alpha_code,full_name,region,area,population)
        db.execSQL("CREATE TABLE " +
                Constants.TABLE_NAME + "(" +
                "id INTEGER PRIMARY KEY," +
                Constants.COL_FLAG + " TEXT," +
                Constants.COL_ALPHA3_CODE + " TEXT," +
                Constants.COL_FULL_NAME + " TEXT," +
                Constants.COL_NAME + " TEXT," +
                Constants.COL_REGION + " TEXT," +
                Constants.COL_POPULATION + " NUMBER," +
                Constants.COL_AREA + " NUMBER," +
                Constants.COL_BORDERS + " TEXT," +
                Constants.COL_CAPITAL + " TEXT," +
                Constants.COL_LAT + " REAL," +
                Constants.COL_LNG + " REAL," +
                Constants.COL_CALLING_CODE + " TEXT," +
                Constants.COL_LANGUAGES + " TEXT," +
                Constants.COL_CURRENCIES + " TEXT" + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    public long addCountry(Country country) {
        Log.d("mytag", "addCountry: " + country);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.COL_FLAG, country.getFlag());
        values.put(Constants.COL_ALPHA3_CODE, country.getAlpha3Code());
        if (country.getAltSpellings().length > 1) {
            values.put(Constants.COL_FULL_NAME, country.getAltSpellings()[1]);
        } else {
            values.put(Constants.COL_FULL_NAME, country.getAltSpellings()[0]);
        }
        values.put(Constants.COL_NAME, country.getName());
        values.put(Constants.COL_REGION, country.getRegion());
        values.put(Constants.COL_POPULATION, country.getPopulation());
        values.put(Constants.COL_AREA, country.getArea());
        values.put(Constants.COL_CAPITAL, country.getCapital());

        values.put(Constants.COL_LAT, country.getLatlng()[0]);
        values.put(Constants.COL_LNG, country.getLatlng()[1]);
        values.put(Constants.COL_BORDERS, arrayToString(country.getBorders()));
        values.put(Constants.COL_CALLING_CODE, arrayToString(country.getCallingCodes()));

        Language[] languages = country.getLanguages();
        Currency[] currencies = country.getCurrencies();
        StringBuilder languageText = new StringBuilder();
        StringBuilder currencyText = new StringBuilder();
        for (Language lang : languages) {
            languageText.append(lang.getName()).append(",");
        }
        for (Currency cur : currencies) {
            currencyText.append(cur.getName()).append(",");
        }
        languageText.deleteCharAt(languageText.length() - 1);
        if (currencyText.length() > 0) {
            currencyText.deleteCharAt(currencyText.length() - 1);
        }

        values.put(Constants.COL_CURRENCIES, currencyText.toString());
        values.put(Constants.COL_LANGUAGES, languageText.toString());

        return db.insert(Constants.TABLE_NAME, null, values);
    }

    public String arrayToString(String[] array) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i != array.length - 1) {
                text.append(array[i]).append(",");
            } else {
                text.append(array[i]);
            }
        }
        return text.toString();
    }

    public void deleteCountry(Country country) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.COL_FLAG + "=?", new String[]{country.getFlag()});
    }

    public ArrayList<Country> getCountryCodes() {
        ArrayList<Country> countryList = new ArrayList<>();
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                count++;
                Country country = new Country();
                country.setName(cursor.getString(cursor.getColumnIndex(Constants.COL_NAME)));
                country.setAlpha3Code(cursor.getString(cursor.getColumnIndex(Constants.COL_ALPHA3_CODE)));
                country.setFlag(cursor.getString(cursor.getColumnIndex(Constants.COL_FLAG)));
                country.setArea(cursor.getDouble(cursor.getColumnIndex(Constants.COL_AREA)));
                country.setPopulation(cursor.getLong(cursor.getColumnIndex(Constants.COL_POPULATION)));
                country.setRegion(cursor.getString(cursor.getColumnIndex(Constants.COL_REGION)));
                country.setAltSpellings(new String[]{cursor.getString(cursor.getColumnIndex(Constants.COL_FULL_NAME))});
                country.setCapital(cursor.getString(cursor.getColumnIndex(Constants.COL_CAPITAL)));
                // lat lng
                double lat = cursor.getDouble(cursor.getColumnIndex(Constants.COL_LAT));
                double lng = cursor.getDouble(cursor.getColumnIndex(Constants.COL_LNG));
                double latlng[] = new double[]{lat, lng};
                country.setLatlng(latlng);

                // Borders
                String borderText = cursor.getString(cursor.getColumnIndex(Constants.COL_BORDERS));
                String[] borders = new String[]{};
                if (borderText.length() > 0) {
                    borders = borderText.split(",");
                }
                country.setBorders(borders);

                // CallingCodes
                String callingCodesText = cursor.getString(cursor.getColumnIndex(Constants.COL_CALLING_CODE));
                String[] callingCodes = new String[]{};
                if (callingCodesText.length() > 0) {
                    callingCodes = callingCodesText.split(",");
                }
                country.setCallingCodes(callingCodes);

                // Currencies
                String currencyText = cursor.getString(cursor.getColumnIndex(Constants.COL_CURRENCIES));
                String[] currencyArray = new String[]{};
                if (currencyText.length() > 0) {
                    currencyArray = currencyText.split(",");
                }
                Currency[] currencies = new Currency[currencyArray.length];
                for (int i = 0; i < currencyArray.length; i++) {
                    Currency currency = new Currency(null, currencyArray[i], null);
                    currencies[i] = currency;
                }
                country.setCurrencies(currencies);


                // Languages
                String languageText = cursor.getString(cursor.getColumnIndex(Constants.COL_LANGUAGES));
                String[] languagesArray = new String[]{};
                if (languageText.length() > 0) {
                    languagesArray = languageText.split(",");
                }
                Language[] languages = new Language[languagesArray.length];
                for (int i = 0; i < languagesArray.length; i++) {
                    Language language = new Language(languagesArray[i]);
                    languages[i] = language;
                }
                country.setLanguages(languages);

                countryList.add(country);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return countryList;
    }


}
