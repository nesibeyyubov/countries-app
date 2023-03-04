package com.nesib.countriesapp.utils

import android.graphics.drawable.Drawable

enum class Region(val value: String, val drawable: Drawable? = null) {
    ASIA("asia"),
    EUROPE("europe"),
    AFRICA("africa"),
    AMERICAS("americas"),
    OCEANIA("oceania"),
}