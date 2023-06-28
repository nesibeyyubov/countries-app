package com.nesib.countriesapp.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nesib.countriesapp.models.*

class TypeConverters {

    /**
     * List<String>
     */
    @TypeConverter
    fun listToJson(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToList(text: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(text, type)
    }


    /**
     * @see CapitalInfoUi
     */
    @TypeConverter
    fun capitalInfoToJson(capitalInfoUi: CapitalInfoUi): String {
        return Gson().toJson(capitalInfoUi)
    }

    @TypeConverter
    fun jsonToCapitalInfo(json: String): CapitalInfoUi {
        return Gson().fromJson(json, CapitalInfoUi::class.java)
    }

    /**
     * @see CarUi
     */
    @TypeConverter
    fun carToJson(carUi: CarUi): String {
        return Gson().toJson(carUi)
    }

    @TypeConverter
    fun jsonToCar(json: String): CarUi {
        return Gson().fromJson(json, CarUi::class.java)
    }

    /**
     * @see CoatOfArmsUi
     */
    @TypeConverter
    fun coatOfArmsToJson(data: CoatOfArmsUi): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun jsonToCoatOfArmsToJson(json: String): CoatOfArmsUi {
        return Gson().fromJson(json, CoatOfArmsUi::class.java)
    }


    /**
     * @see FlagsUi
     */
    @TypeConverter
    fun flagsToJson(data: FlagsUi): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun jsonToFlags(json: String): FlagsUi {
        return Gson().fromJson(json, FlagsUi::class.java)
    }

    /**
     * @see PhoneDetailsUi
     */
    @TypeConverter
    fun phoneDetailsToJson(data: PhoneDetailsUi): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun jsonToPhoneDetails(json: String): PhoneDetailsUi {
        return Gson().fromJson(json, PhoneDetailsUi::class.java)
    }

    /**
     *  List<Double>
     */
    @TypeConverter
    fun latlngToJson(data: List<Double>): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun jsonToLatlng(json: String): List<Double> {
        val type = object : TypeToken<List<Double>>() {}.type
        return Gson().fromJson(json, type)
    }

    /**
     * @see MapsUi
     */
    @TypeConverter
    fun mapsToJson(data: MapsUi): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun jsonToMaps(json: String): MapsUi {
        return Gson().fromJson(json, MapsUi::class.java)
    }

    /**
     * @see NameUi
     */
    @TypeConverter
    fun nameToJson(data: NameUi): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun jsonToName(json: String): NameUi {
        return Gson().fromJson(json, NameUi::class.java)
    }

    /**
     * @see PostalCodeUi
     */
    @TypeConverter
    fun postalCodeToJson(data: PostalCodeUi): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun jsonToPostalCode(json: String): PostalCodeUi {
        return Gson().fromJson(json, PostalCodeUi::class.java)
    }

    /**
     * @see CurrencyUi
     */
    @TypeConverter
    fun currencyToJson(data: CurrencyUi): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun jsonToCurrency(json: String): CurrencyUi {
        return Gson().fromJson(json, CurrencyUi::class.java)
    }

    /**
     * @see TranslationUi
     */
    @TypeConverter
    fun translationToJson(data: TranslationUi): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun jsonToTranslation(json: String): TranslationUi {
        return Gson().fromJson(json, TranslationUi::class.java)
    }


    /**
     *  Map<String,TranslationUi>
     */
    @TypeConverter
    fun translationMapToJson(data: Map<String, TranslationUi>): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun jsonToTranslationMap(json: String): Map<String, TranslationUi> {
        val type = object : TypeToken<Map<String, TranslationUi>>() {}.type
        return Gson().fromJson(json, type)
    }

    /**
     *  Map<String, CurrencyUi>
     */
    @TypeConverter
    fun currenciesMapToJson(data: Map<String, CurrencyUi>): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun jsonToCurrenciesMap(json: String): Map<String, CurrencyUi> {
        val type = object : TypeToken<Map<String, CurrencyUi>>() {}.type
        return Gson().fromJson(json, type)
    }

    /**
     *  Map<String, String>
     */
    @TypeConverter
    fun stringStringMapToJson(data: Map<String, String>): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun jsonStringStringMap(json: String): Map<String, String> {
        val type = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(json, type)
    }


}