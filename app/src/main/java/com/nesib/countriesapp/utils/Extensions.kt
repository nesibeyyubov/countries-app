package com.nesib.countriesapp.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.navigation.navOptions
import com.nesib.countriesapp.R
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

val Activity.sdkVersion
    get() = Build.VERSION.SDK_INT


val Context.dataStore by preferencesDataStore(name = "score_prefs")

val slideInOutAnimationNavOptions = navOptions {
    anim {
        enter = R.anim.slide_in_right
        exit = R.anim.slide_out_left
        popEnter = R.anim.slide_in_left
        popExit = R.anim.slide_out_right
    }
}

val slideUpDownAnimationNavOptions = navOptions {
    anim {
        enter = R.anim.slide_up
        popExit = R.anim.slide_down
    }
}


/**
 * Common
 */

fun Int?.toFormattedDecimal(): String {
    if (this == null) return "0"

    return DecimalFormat("#,###")
        .format(this)
        .replace(",", ".")
}


fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    // For 29 api or above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
    return (connectivityManager.activeNetworkInfo != null
            && connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true)

}