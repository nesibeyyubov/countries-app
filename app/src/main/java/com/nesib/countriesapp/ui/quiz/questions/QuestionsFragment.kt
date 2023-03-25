package com.nesib.countriesapp.ui.quiz.questions

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.nesib.countriesapp.base.BaseFragment
import com.nesib.countriesapp.base.ScreenParams
import com.nesib.countriesapp.databinding.FragmentQuestionBinding
import com.nesib.countriesapp.databinding.FragmentSearchBinding
import com.nesib.countriesapp.models.QuizType
import com.nesib.countriesapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionsFragment :
    BaseFragment<FragmentQuestionBinding, QuestionsState, QuestionsViewModel, QuestionsFragment.Params>() {

    override val viewModel: QuestionsViewModel
        get() = ViewModelProvider(this)[QuestionsViewModel::class.java]

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentQuestionBinding
        .inflate(inflater, container, false)

    private val options by lazy {
        listOf(
            binding.optionAText,
            binding.optionBText,
            binding.optionCText,
            binding.optionDText
        )
    }

    override fun initViews() {
        viewModel.getQuestions()
    }

    override fun render(state: QuestionsState): Unit = with(binding) {
        Log.d("mytag", "render: ${state.currentQuestion}")
        state.currentQuestion?.let { currentQuestion ->
            questionNumber.text = if (state.loading) "Loading..." else "0"
            loadingAnimation.isVisible = state.loading

            if (state.loading.not()) {
                questionText.text = currentQuestion.title
                questionImage.load(currentQuestion.title)

                options.forEachIndexed { index, optionTextView ->
                    optionTextView.text = currentQuestion.options[index]
                }
                optionsContainer.visible()
            }

        }

    }


    data class Params(val quizType: QuizType) : ScreenParams

}