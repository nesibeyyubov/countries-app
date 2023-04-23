package com.nesib.countriesapp.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class BaseAndroidViewModel<STATE : State>(initialState: STATE, myApplication: Application) :
    AndroidViewModel(myApplication!!) {

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    /**
     * @param update function is used to change state value
     */
    fun setState(update: (old: STATE) -> STATE) {
        _state.update(update)
    }


    fun currentState() = state.value


}