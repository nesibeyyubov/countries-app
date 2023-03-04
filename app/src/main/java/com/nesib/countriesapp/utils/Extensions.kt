package com.nesib.countriesapp.utils

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

fun String?.toSafeString() = this ?: ""
fun Boolean?.toSafeBoolean() = this ?: false

fun <T> List<T>?.toSafeList() = this ?: emptyList()


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