package com.nesib.countriesapp.models

data class Quiz(
    var title: String? = null,
    var subTitle: String? = null,
    var duration: String? = null,
    var bestScore:Int = 0,
    var questionCount:Int = 0,
    var quizType: String? = null,
)