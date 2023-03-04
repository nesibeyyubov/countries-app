package com.nesib.countriesapp.ui

import android.provider.ContactsContract.Data

sealed class DataState<T>(data: T? = null, message: String? = null) {

    data class Success<T>(val data: T) : DataState<T>(data)
    class Error<T> : DataState<T>(message = "Error text here")
    object Loading : DataState<Any>()

}