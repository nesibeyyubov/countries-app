package com.nesib.countriesapp.models

import com.nesib.countriesapp.utils.toSafeBoolean
import com.nesib.countriesapp.utils.toSafeList
import com.nesib.countriesapp.utils.toSafeString

data class CountryModel(
    val altSpellings: List<String>?,
    val area: Double?,
    val borders: List<String>?,
    val capital: List<String>?,
    val capitalInfo: CapitalInfo?,
    val car: Car?,
    val coatOfArms: CoatOfArms?,
    val continents: List<String>?,
    val fifa: String?,
    val flags: Flags?,
    val idd: Idd?,
    val independent: Boolean?,
    val landlocked: Boolean?,
    val latlng: List<Double>?,
    val maps: Maps?,
    val name: Name?,
    val population: Int?,
    val region: String?,
    val startOfWeek: String?,
    val status: String?,
    val subregion: String?,
    val timezones: List<String>?,
    val tld: List<String>?,
    val unMember: Boolean?,
    val translations: Map<String, Translation>?,
    val currencies: Map<String, Currency>?,
    val languages: Map<String, String>?,
    val postalCode: PostalCode?
)


fun CountryModel.toUiModel(): CountryUi {
    return CountryUi(
        altSpellings = this.altSpellings.toSafeList(),
        area = this.area ?: 0.0,
        borders = this.borders.toSafeList(),
        capital = this.capital.toSafeList(),
        capitalInfo = this.capitalInfo.toUiModel(),
        car = this.car.toUiModel(),
        coatOfArms = this.coatOfArms.toUiModel(),
        continents = this.continents.toSafeList(),
        fifaCode = this.fifa.toSafeString(),
        flags = this.flags.toUiModel(),
        phoneNumberDetails = this.idd.toUiModel(),
        independent = this.independent.toSafeBoolean(),
        landlocked = this.landlocked.toSafeBoolean(),
        latlng = this.latlng.toSafeList(),
        maps = this.maps.toUiModel(),
        name = this.name.toUiModel(),
        population = this.population ?: 0,
        region = this.region.toSafeString(),
        startOfWeek = this.startOfWeek.toSafeString(),
        status = this.status.toSafeString(),
        subregion = this.subregion.toSafeString(),
        timezones = this.timezones.toSafeList(),
        websiteEndingDomains = this.tld.toSafeList(),
        unMember = this.unMember.toSafeBoolean(),
        translations = this.translations.toUiModel(),
        currencies = this.currencies.toUiModel2(),
        languages = this.languages ?: emptyMap(),
        postalCode = this.postalCode.toUiModel()
    )
}

fun Map<String, Translation>?.toUiModel(): Map<String, TranslationUi> {
    if (this == null) return emptyMap()
    val newMap = hashMapOf<String, TranslationUi>()
    this.forEach { entry ->
        newMap[entry.key] = entry.value.toUiModel()
    }
    return newMap
}

fun Map<String, Currency>?.toUiModel2(): Map<String, CurrencyUi> {
    if (this == null) return emptyMap()
    val newMap = hashMapOf<String, CurrencyUi>()
    this.forEach { entry ->
        newMap[entry.key] = entry.value.toUiModel()
    }
    return newMap
}


data class CapitalInfo(
    val latlng: List<Double>?
)

fun CapitalInfo?.toUiModel() = CapitalInfoUi(latlng = this?.latlng ?: this?.latlng ?: emptyList())


data class Car(
    val side: String?,
    val signs: List<String>?
)

fun Car?.toUiModel() = CarUi(side = this?.side.toSafeString(), signs = this?.signs ?: emptyList())


data class CoatOfArms(
    val png: String?,
    val svg: String?
)

fun CoatOfArms?.toUiModel() = CoatOfArmsUi(pngFormat = this?.png.toSafeString(), svgFormat = this?.svg.toSafeString())


data class Currency(
    val name: String?,
    val symbol: String?
)

fun Currency?.toUiModel() = CurrencyUi(name = this?.name.toSafeString(), symbol = this?.symbol.toSafeString())


data class Flags(
    val alt: String?,
    val png: String?,
    val svg: String?
)

fun Flags?.toUiModel() =
    FlagsUi(
        description = this?.alt.toSafeString(),
        pngFormat = this?.png.toSafeString(),
        svgFormat = this?.svg.toSafeString()
    )

data class Idd(
    val root: String?,
    val suffixes: List<String>?
)

fun Idd?.toUiModel() =
    PhoneDetailsUi(root = this?.root.toSafeString(), suffixes = this?.suffixes ?: emptyList())

data class Maps(
    val googleMaps: String?,
    val openStreetMaps: String?
)

fun Maps?.toUiModel() =
    MapsUi(googleMaps = this?.googleMaps.toSafeString(), openStreetMaps = this?.openStreetMaps.toSafeString())

data class Name(
    val common: String?,
    val official: String?
)

fun Name?.toUiModel() = NameUi(
    common = this?.common.toSafeString(),
    official = this?.official.toSafeString()
)

data class Translation(
    val common: String?,
    val official: String?
)

fun Translation?.toUiModel() = TranslationUi(
    common = this?.common.toSafeString(),
    official = this?.official.toSafeString()
)

data class PostalCode(
    val format: String?
)

fun PostalCode?.toUiModel() = PostalCodeUi(
    format = this?.format.toSafeString()
)
