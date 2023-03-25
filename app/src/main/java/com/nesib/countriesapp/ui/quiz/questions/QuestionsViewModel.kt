package com.nesib.countriesapp.ui.quiz.questions

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.nesib.countriesapp.base.BaseViewModel
import com.nesib.countriesapp.data.network.CountriesRepository
import com.nesib.countriesapp.models.CountryUi
import com.nesib.countriesapp.models.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val countriesRepository: CountriesRepository
) : BaseViewModel<QuestionsState>(QuestionsState()) {

    fun getQuestions() {
        countriesRepository.getAllCountries()
            .onStart { setState { it.copy(loading = true) } }
            .onEach {
                makeQuestions(it.toMutableList())
            }
//            .catch { }
            .launchIn(viewModelScope)
    }

    fun nextQuestion() {
        val questions = currentState().questions.toMutableList()
        val randomQuestion = questions.random()
        questions.remove(randomQuestion)
        setState { it.copy(loading = false, currentQuestion = randomQuestion, questions = questions) }
    }

    private fun makeQuestions(countries: MutableList<CountryUi>) {
        val questions = mutableListOf<Question>()
        while (countries.size >= 4) {
            val answerCountry = countries.random()
            countries.remove(answerCountry)

            val option1 = countries.random()
            countries.remove(option1)

            val option2 = countries.random()
            countries.remove(option2)

            val option3 = countries.random()
            countries.remove(option3)


            Question(
                title = answerCountry.flags.pngFormat,
                options = listOf(
                    answerCountry.name.common,
                    option1.name.common,
                    option2.name.common,
                    option3.name.common
                ).shuffled(),
                answer = answerCountry.name.common
            ).also { questions.add(it) }

            countries.shuffle()
        }
        Log.d("mytag", "question count: ${questions.size}")
        setState { it.copy(questions = questions) }
        nextQuestion()
    }

}