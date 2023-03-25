package com.nesib.countriesapp.utils

import android.content.Context
import android.os.Build
import android.os.Build.VERSION
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import java.text.DecimalFormat

/**
 * Nullability
 */
fun String?.toSafeString() = this ?: ""
fun Boolean?.toSafeBoolean() = this ?: false

fun <T> List<T>?.toSafeList() = this ?: emptyList()


/**
 * Visibility
 */
fun View.visible() {
    this.isVisible = true
}

fun View.invisible() {
    this.isInvisible = true
}

fun View.gone() {
    this.isGone = true
}

fun List<View>.visible() {
    this.forEach { it.visible() }
}

fun List<View>.invisible() {
    this.forEach { it.invisible() }
}

fun List<View>.gone() {
    this.forEach { it.gone() }
}


/**
 * SDK extensions
 */

fun Int.supportsChangingStatusBarColors(): Boolean {
    return this >= Build.VERSION_CODES.M
}


/**
 * Fragment
 */

val Fragment.sdkVersion
    get() = Build.VERSION.SDK_INT


val Context.dataStore by preferencesDataStore(name = "score_prefs")


/**
 * Common
 */

fun Int?.toFormattedDecimal(): String {
    if (this == null) return "0"

    return DecimalFormat("#,###")
        .format(this)
        .replace(",", ".")
}