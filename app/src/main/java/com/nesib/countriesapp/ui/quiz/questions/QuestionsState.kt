package com.nesib.countriesapp.ui.quiz.questions

import com.nesib.countriesapp.base.State
import com.nesib.countriesapp.models.Question

data class QuestionsState(
    val loading: Boolean = false,
    val questions: List<Question> = emptyList(),
    val currentQuestion: Question? = null,
    val currentQuestionNumber: Int = 0
) : State