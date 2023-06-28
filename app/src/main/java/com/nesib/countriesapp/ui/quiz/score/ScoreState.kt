package com.nesib.countriesapp.ui.quiz.score

import com.nesib.countriesapp.base.State

data class ScoreState(
    val loading: Boolean = false,
    val hasError: Boolean = false,
    val bestScore: Int = 0
) : State