package com.nesib.countriesapp.utils

import android.content.Context
import androidx.annotation.DrawableRes
import com.nesib.countriesapp.R

enum class Region(val value: String, @DrawableRes val drawableRes: Int) {
    ASIA("asia", R.drawable.bg_asia),
    EUROPE("europe", R.drawable.bg_europa),
    AFRICA("africa", R.drawable.bg_africa),
    AMERICAS("americas", R.drawable.bg_america),
    OCEANIA("oceania", R.drawable.bg_oceania),
    ALL_COUNTRIES("all_countries", R.drawable.bg_world)
}

fun Region.toTranslatedText(context: Context): String {
    return when (this) {
        Region.ASIA -> context.getString(R.string.region_asia)
        Region.AFRICA -> context.getString(R.string.region_africa)
        Region.AMERICAS -> context.getString(R.string.region_americas)
        Region.EUROPE -> context.getString(R.string.region_europe)
        Region.OCEANIA -> context.getString(R.string.region_oceania)
        Region.ALL_COUNTRIES -> context.getString(R.string.region_asia)
    }
}