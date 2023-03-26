package com.nesib.countriesapp.ui.quiz.questions

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.viewModelScope
import com.nesib.countriesapp.R
import com.nesib.countriesapp.base.BaseViewModel
import com.nesib.countriesapp.data.local.preferences.PreferencesDataStore
import com.nesib.countriesapp.data.network.CountriesRepository
import com.nesib.countriesapp.models.CountryUi
import com.nesib.countriesapp.models.Question
import com.nesib.countriesapp.models.QuizType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Timer
import javax.inject.Inject
import kotlin.time.seconds

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val countriesRepository: CountriesRepository,
    private val preferencesDataStore: PreferencesDataStore
) : BaseViewModel<QuestionsState>(QuestionsState()) {

    companion object {
        const val QUIZ_TIME = 20
    }

    private val _timer = MutableStateFlow(QUIZ_TIME)
    val timer = _timer.asStateFlow()

    private var countDownTimer: CountDownTimer? = null

    private var countDownTimerStarted = false

    private var rightAnswerCount = 0

    fun getRightAnswerCount() = rightAnswerCount

    fun getQuestions(quizTypeFlag: QuizType?, context: Context) {
        countriesRepository.getAllCountries()
            .onStart { setState { it.copy(loading = true) } }
            .onEach {
                makeQuestions(it.toMutableList(), quizTypeFlag, context)
            }
            .catch { }
            .launchIn(viewModelScope)
    }


    fun saveBestScore(quizType: QuizType) {
        preferencesDataStore.getScorePreferences()
            .onStart { }
            .catch { }
            .onEach {
                when (quizType) {
                    QuizType.Capitals -> {
                        if (rightAnswerCount > it.capitalsScore) {
                            preferencesDataStore.setCapitalsScore(rightAnswerCount)
                        }
                    }
                    QuizType.Regions -> {
                        if (rightAnswerCount > it.regionsScore) {
                            preferencesDataStore.setCapitalsScore(rightAnswerCount)
                        }
                    }
                    QuizType.Flags -> {
                        if (rightAnswerCount > it.flagsScore) {
                            preferencesDataStore.setCapitalsScore(rightAnswerCount)
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun startTimer() {
        if (countDownTimerStarted) return
        countDownTimer = object : CountDownTimer(QUIZ_TIME * 1000L, 1 * 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000).toInt()
                _timer.update { secondsLeft }
            }

            override fun onFinish() {
                _timer.update { 0 }
            }
        }
        countDownTimer?.start()
        countDownTimerStarted = true
    }

    fun cancelTimer() {
        countDownTimer?.cancel()
    }

    fun checkAnswer(optionText: String): Boolean {
        val isRight = optionText == currentState().currentQuestion?.answer
        if (isRight) {
            rightAnswerCount++
        }
        return isRight
    }

    fun getRightAnswerIndex() =
        currentState().currentQuestion?.options?.indexOf(currentState().currentQuestion?.answer)

    fun nextQuestion(forceNext: Boolean = false) {
        val questions = currentState().questions.toMutableList()
        val randomQuestion = questions.random()
        questions.remove(randomQuestion)
        setState {
            it.copy(
                loading = false,
                currentQuestion = randomQuestion,
                questions = questions,
                currentQuestionNumber = if (forceNext) it.currentQuestionNumber else it.currentQuestionNumber + 1
            )
        }
        startTimer()
    }

    private fun makeQuestions(countries: MutableList<CountryUi>, quizType: QuizType?, context: Context) {
        val questions = mutableListOf<Question>()
        while (countries.size >= 4) {
            val rightOption = countries.random()
            countries.remove(rightOption)

            val wrongOption1 = countries.random()
            countries.remove(wrongOption1)

            val wrongOption2 = countries.random()
            countries.remove(wrongOption2)

            val wrongOption3 = countries.random()
            countries.remove(wrongOption3)

            val questionTitle = when (quizType) {
                QuizType.Flags -> rightOption.flags.pngFormat
                QuizType.Regions -> context.getString(R.string.question_region_text, rightOption.region)
                QuizType.Capitals -> {
                    val capital = if (rightOption.capital.isNotEmpty()) rightOption.capital.first() else null

                    if (capital == null) {
                        "... is the capital of itself"
                    } else {
                        context.getString(
                            R.string.question_capital_text,
                            capital
                        )
                    }

                }
                null -> ""
            }

            Question(
                title = questionTitle,
                options = listOf(
                    rightOption.name.common,
                    wrongOption1.name.common,
                    wrongOption2.name.common,
                    wrongOption3.name.common
                ).shuffled(),
                answer = rightOption.name.common
            ).also { questions.add(it) }

            countries.shuffle()
        }
        setState { it.copy(questions = questions, loading = false) }
    }

}