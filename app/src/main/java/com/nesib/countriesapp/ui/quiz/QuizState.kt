package com.nesib.countriesapp.ui.quiz

import com.nesib.countriesapp.base.State
import com.nesib.countriesapp.models.Quiz

data class QuizState(val loading: Boolean = false, val quizzes: List<Quiz> = emptyList()) : State