package com.nesib.countriesapp.database

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import com.nesib.countriesapp.models.Country
import android.content.ContentValues
import android.content.Context
import com.nesib.countriesapp.utils.Constants
import com.nesib.countriesapp.models.Currency
import com.nesib.countriesapp.models.Language
import java.lang.StringBuilder
import java.util.ArrayList
import kotlin.jvm.Synchronized

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, Constants.DB_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        // CREATE TABLE countries_table(id,flag,alpha_code,full_name,region,area,population)
        db.execSQL(
            "CREATE TABLE " +
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
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME)
        onCreate(db)
    }

    fun addCountry(country: Country) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(Constants.COL_FLAG, country.flag)
        values.put(Constants.COL_ALPHA3_CODE, country.alpha3Code)
        if (country.altSpellings.size > 1) {
            values.put(Constants.COL_FULL_NAME, country.altSpellings[1])
        } else {
            values.put(Constants.COL_FULL_NAME, country.altSpellings[0])
        }
        values.put(Constants.COL_NAME, country.name)
        values.put(Constants.COL_REGION, country.region)
        values.put(Constants.COL_POPULATION, country.population)
        values.put(Constants.COL_AREA, country.area)
        values.put(Constants.COL_CAPITAL, country.capital)
        values.put(Constants.COL_LAT, country.latlng[0])
        values.put(Constants.COL_LNG, country.latlng[1])
        values.put(Constants.COL_BORDERS, arrayToString(country.borders))
        values.put(Constants.COL_CALLING_CODE, arrayToString(country.callingCodes))
        val languages = country.languages
        val currencies = country.currencies
        val languageText = StringBuilder()
        val currencyText = StringBuilder()
        for (lang in languages) {
            languageText.append(lang.name).append(",")
        }
        for (cur in currencies) {
            currencyText.append(cur.name).append(",")
        }
        languageText.deleteCharAt(languageText.length - 1)
        if (currencyText.isNotEmpty()) {
            currencyText.deleteCharAt(currencyText.length - 1)
        }
        values.put(Constants.COL_CURRENCIES, currencyText.toString())
        values.put(Constants.COL_LANGUAGES, languageText.toString())
        db.insert(Constants.TABLE_NAME, null, values)
    }

    private fun arrayToString(array: List<String>): String {
        val text = StringBuilder()
        for (i in array.indices) {
            if (i != array.size - 1) {
                text.append(array[i]).append(",")
            } else {
                text.append(array[i])
            }
        }
        return text.toString()
    }

    fun deleteCountry(country: Country) {
        val db = writableDatabase
        db.delete(Constants.TABLE_NAME, Constants.COL_FLAG + "=?", arrayOf(country.flag))
    }

    // lat lng
    val countryCodes:

    // Borders

    // CallingCodes

    // Currencies


    // Languages
            ArrayList<Country>
        get() {
            val countryList = ArrayList<Country>()
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT * FROM " + Constants.TABLE_NAME, null)
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val country = Country()
                    country.name = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_NAME))
                    country.alpha3Code =
                        cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_ALPHA3_CODE))
                    country.flag = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_FLAG))
                    country.area = cursor.getDouble(cursor.getColumnIndexOrThrow(Constants.COL_AREA))
                    country.population =
                        cursor.getLong(cursor.getColumnIndexOrThrow(Constants.COL_POPULATION))
                    country.region = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_REGION))
                    country.altSpellings = listOf(
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                Constants.COL_FULL_NAME
                            )
                        )
                    )
                    country.capital = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_CAPITAL))
                    // lat lng
                    val lat = cursor.getDouble(cursor.getColumnIndexOrThrow(Constants.COL_LAT))
                    val lng = cursor.getDouble(cursor.getColumnIndexOrThrow(Constants.COL_LNG))
                    val latlng = listOf<Double>(lat,lng)
                    country.latlng = latlng

                    // Borders
                    val borderText = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_BORDERS))
                    var borders = listOf<String>()
                    if (borderText.isNotEmpty()) {
                        borders = borderText.split(",".toRegex())
                    }
                    country.borders = borders

                    // CallingCodes
                    val callingCodesText =
                        cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_CALLING_CODE))
                    var callingCodes = listOf<String>()
                    if (callingCodesText.isNotEmpty()) {
                        callingCodes = callingCodesText.split(",".toRegex())
                    }
                    country.callingCodes = callingCodes

                    // Currencies
                    val currencyText =
                        cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_CURRENCIES))
                    var currencyArray = arrayOf<String?>()
                    if (currencyText.isNotEmpty()) {
                        currencyArray = currencyText.split(",".toRegex()).toTypedArray()
                    }
                    val currencies = mutableListOf<Currency>()
                    for (i in currencyArray.indices) {
                        val currency = Currency(null, currencyArray[i], null)
                        currencies.add(currency)
                    }
                    country.currencies = currencies


                    // Languages
                    val languageText =
                        cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_LANGUAGES))
                    var languagesArray = arrayOf<String?>()
                    if (languageText.isNotEmpty()) {
                        languagesArray = languageText.split(",".toRegex()).toTypedArray()
                    }
                    val languages = mutableListOf<Language>()
                    for (i in languagesArray.indices) {
                        val language = Language(languagesArray[i]!!)
                        languages.add(language)
                    }
                    country.languages = languages
                    countryList.add(country)
                    cursor.moveToNext()
                }
            }
            cursor.close()
            return countryList
        }

    companion object {
        private var instance: DatabaseHelper? = null
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context?): DatabaseHelper {
            if (instance == null) {
                instance = DatabaseHelper(context)
            }
            return instance!!
        }
    }
}