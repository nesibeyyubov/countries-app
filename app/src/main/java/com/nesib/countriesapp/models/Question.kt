package com.nesib.countriesapp.models

data class Question(
    val title: String,
    val options: List<String>,
    val answer: String
)