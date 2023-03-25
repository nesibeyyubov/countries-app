package com.nesib.countriesapp.models

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.nesib.countriesapp.R

data class Quiz(
    @StringRes var title: Int,
    @StringRes var subTitle: Int,
    var bestScore: Int = 0,
    var questionCount: Int = 0,
    var quizType: QuizType,
)

enum class QuizType(
    @ColorRes val backgroundColorResource: Int
) {
    Regions(R.color.colorBlue),
    Flags(R.color.colorOrangeDark),
    Capitals(R.color.colorPrimary)
}