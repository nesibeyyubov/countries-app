package com.nesib.countriesapp.ui.quiz

import androidx.lifecycle.viewModelScope
import com.nesib.countriesapp.R
import com.nesib.countriesapp.base.BaseViewModel
import com.nesib.countriesapp.data.local.preferences.PreferencesDataStore
import com.nesib.countriesapp.models.Quiz
import com.nesib.countriesapp.models.QuizType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
) : BaseViewModel<QuizState>(QuizState()) {


    fun getQuizzes() {
        preferencesDataStore.getScorePreferences()
            .onStart { setState { it.copy(loading = true) } }
            .onEach { scorePreference ->
                val quizzes = listOf(
                    Quiz(
                        title = R.string.quiz_title_capital_text,
                        subTitle = R.string.quiz_subtitle_capital_text,
                        questionCount = 500,
                        bestScore = scorePreference.capitalsScore,
                        quizType = QuizType.Capitals,
                    ),
                    Quiz(
                        title = R.string.quiz_title_regions_text,
                        subTitle = R.string.quiz_subtitle_regions_text,
                        questionCount = 500,
                        bestScore = scorePreference.regionsScore,
                        quizType = QuizType.Regions,
                    ),
                    Quiz(
                        title = R.string.quiz_title_flags_text,
                        subTitle = R.string.quiz_subtitle_flags_text,
                        questionCount = 500,
                        bestScore = scorePreference.flagsScore,
                        quizType = QuizType.Flags,
                    )
                )
                setState { it.copy(loading = false, quizzes = quizzes) }
            }
            .catch {
                val quizzes = listOf(
                    Quiz(
                        title = R.string.quiz_title_capital_text,
                        subTitle = R.string.quiz_subtitle_capital_text,
                        questionCount = 500,
                        bestScore = 0,
                        quizType = QuizType.Capitals,
                    ),
                    Quiz(
                        title = R.string.quiz_title_regions_text,
                        subTitle = R.string.quiz_subtitle_regions_text,
                        questionCount = 500,
                        bestScore = 0,
                        quizType = QuizType.Regions,
                    ),
                    Quiz(
                        title = R.string.quiz_title_flags_text,
                        subTitle = R.string.quiz_subtitle_flags_text,
                        questionCount = 500,
                        bestScore = 0,
                        quizType = QuizType.Flags,
                    )
                )
                setState { it.copy(loading = false, quizzes = quizzes) }
            }
            .launchIn(viewModelScope)

    }

}