package com.nesib.countriesapp.models

data class CountryUi(
    val altSpellings: List<String>,
    val area: Double,
    val borders: List<String>,
    val capital: List<String>,
    val capitalInfo: CapitalInfoUi,
    val car: CarUi,
    val coatOfArms: CoatOfArmsUi,
    val continents: List<String>,
    val currencies: Map<String, CurrencyUi>,
    val fifaCode: String,
    val flags: FlagsUi,
    val phoneNumberDetails: PhoneDetailsUi,
    val independent: Boolean,
    val landlocked: Boolean,
    val languages: Map<String, String>,
    val latlng: List<Double>,
    val maps: MapsUi,
    val name: NameUi,
    val population: Int,
    val region: String,
    val startOfWeek: String,
    val status: String,
    val subregion: String,
    val timezones: List<String>,
    val websiteEndingDomains: List<String>,
    val translations: Map<String, TranslationUi>,
    val unMember: Boolean,
    val postalCode: PostalCodeUi
)

fun CountryUi.toDetailsKeyValue(): List<CountryDetail> {
    val details = mutableListOf<CountryDetail>()
    details.add(CountryDetail("Currency", currencies.keys.first()))
    details.add(CountryDetail("Region", region))
    details.add(CountryDetail("Subregion", subregion))
    details.add(CountryDetail("Timezones", timezones.joinToString("\n")))
    details.add(CountryDetail("Start Of Week", startOfWeek))
    details.add(CountryDetail("Postal Code", postalCode.format))
    details.add(CountryDetail("Calling Code", "${phoneNumberDetails.root}${phoneNumberDetails.suffixes.first()}"))
    details.add(CountryDetail("Language", languages[languages.keys.first()] ?: languages.keys.first()))
    return details.filter { it.value.isNotEmpty() }
}

data class CapitalInfoUi(
    val latlng: List<Double>
)

data class CarUi(
    val side: String, val signs: List<String>
)

data class CoatOfArmsUi(
    val pngFormat: String, val svgFormat: String
)

data class CurrencyUi(
    val name: String, val symbol: String
)

data class PostalCodeUi(
    val format: String
)

data class FlagsUi(
    val description: String, val pngFormat: String, val svgFormat: String
)

data class PhoneDetailsUi(
    val root: String, val suffixes: List<String>
)

data class MapsUi(
    val googleMaps: String, val openStreetMaps: String
)

data class NameUi(
    val common: String, val official: String
)

data class TranslationUi(
    val common: String, val official: String
)