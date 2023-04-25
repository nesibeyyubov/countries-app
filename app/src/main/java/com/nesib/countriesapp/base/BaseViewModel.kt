package com.nesib.countriesapp.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class BaseViewModel<STATE : State>(initialState: STATE) : ViewModel() {

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