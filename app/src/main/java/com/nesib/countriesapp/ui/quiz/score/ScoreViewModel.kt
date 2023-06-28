package com.nesib.countriesapp.ui.quiz.score

import androidx.lifecycle.viewModelScope
import com.nesib.countriesapp.base.BaseViewModel
import com.nesib.countriesapp.data.local.preferences.PreferencesDataStore
import com.nesib.countriesapp.models.QuizType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreViewModel @Inject constructor(
    private val dataStore: PreferencesDataStore
) : BaseViewModel<ScoreState>(ScoreState()) {

    fun getBestScore(quizType: QuizType?) {
        if (quizType == null) return
        dataStore.getScorePreferences()
            .onEach {
                val bestScore = when (quizType) {
                    QuizType.Capitals -> it.capitalsScore
                    QuizType.Regions -> it.regionsScore
                    QuizType.Flags -> it.flagsScore
                }
                setState { prevState -> prevState.copy(loading = false, hasError = false, bestScore = bestScore) }
            }
            .catch { setState { it.copy(loading = false, hasError = true) } }
            .launchIn(viewModelScope)
    }

    fun saveBestScore(quizType: QuizType?, newScore: Int?) = viewModelScope.launch {
        if (quizType == null || newScore == null) return@launch
        try {
            when (quizType) {
                QuizType.Capitals -> {
                    dataStore.setCapitalsScore(newScore)
                }
                QuizType.Flags -> {
                    dataStore.setFlagsScore(newScore)
                }
                QuizType.Regions -> {
                    dataStore.setRegionsScore(newScore)
                }
            }

        } catch (e: Exception) {
        }
    }

}