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
import com.nesib.countriesapp.utils.Constants.QUIZ_DURATION
import com.nesib.countriesapp.utils.NetworkNotAvailableException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val countriesRepository: CountriesRepository,
    private val preferencesDataStore: PreferencesDataStore
) : BaseViewModel<QuestionsState>(QuestionsState()) {

    private val _timer = MutableStateFlow(QUIZ_DURATION / 1000)
    val timer = _timer.asStateFlow()

    private var countDownTimer: CountDownTimer? = null

    private var countDownTimerStarted = false

    private var rightAnswerCount = 0

    fun getRightAnswerCount() = rightAnswerCount

    private fun handleError(error: Throwable) {
        when (error) {
            is NetworkNotAvailableException -> {
                setState { it.copy(error = R.string.error_network_not_available) }
            }
            else -> {
                setState { it.copy(error = R.string.error_something_wron) }
            }
        }
    }

    fun getQuestions(quizTypeFlag: QuizType?, context: Context) {
        countriesRepository.getAllCountries()
            .onStart { setState { it.copy(loading = true) } }
            .onEach {
                makeQuestions(it.toMutableList(), quizTypeFlag, context)
            }
            .catch { handleError(it) }
            .launchIn(viewModelScope)
    }


    fun saveBestScore(quizType: QuizType?) {
        if (quizType == null) return
        preferencesDataStore.getScorePreferences()
            .onStart { }
            .catch {
                setState { state -> state.copy(readyForNavigatingToScoreFragment = true) }
            }
            .onEach {
                when (quizType) {
                    QuizType.Capitals -> {
                        if (rightAnswerCount > it.capitalsScore) {
                            preferencesDataStore.setCapitalsScore(rightAnswerCount)
                        }
                    }
                    QuizType.Regions -> {
                        if (rightAnswerCount > it.regionsScore) {
                            preferencesDataStore.setRegionsScore(rightAnswerCount)
                        }
                    }
                    QuizType.Flags -> {
                        if (rightAnswerCount > it.flagsScore) {
                            preferencesDataStore.setFlagsScore(rightAnswerCount)
                        }
                    }
                }
                setState { state -> state.copy(readyForNavigatingToScoreFragment = true) }
            }
            .launchIn(viewModelScope)
    }

    private fun startTimer() {
        if (countDownTimerStarted) return
        countDownTimer = object : CountDownTimer(QUIZ_DURATION.toLong(), 1 * 1000) {
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

            val wrongOption1 =
                countries.filter { if (quizType == QuizType.Regions) it.region != rightOption.region else true }
                    .random()
            countries.remove(wrongOption1)

            val wrongOption2 =
                countries.filter { if (quizType == QuizType.Regions) it.region != rightOption.region else true }
                    .random()
            countries.remove(wrongOption2)

            val wrongOption3 =
                countries.filter { if (quizType == QuizType.Regions) it.region != rightOption.region else true }
                    .random()
            countries.remove(wrongOption3)

            val questionTitle = when (quizType) {
                QuizType.Flags -> rightOption.flags.pngFormat
                QuizType.Regions -> context.getString(R.string.question_region_text, rightOption.region)
                QuizType.Capitals -> {
                    val capital = if (rightOption.capital.isNotEmpty()) rightOption.capital.first() else null

                    if (capital == null) {
                        context.getString(R.string.capital_of_itself)
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