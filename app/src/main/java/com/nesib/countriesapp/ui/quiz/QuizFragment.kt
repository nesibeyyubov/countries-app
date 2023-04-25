package com.nesib.countriesapp.ui.quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.nesib.countriesapp.R
import com.nesib.countriesapp.base.BaseFragment
import com.nesib.countriesapp.base.ScreenParams
import com.nesib.countriesapp.databinding.FragmentQuizBinding
import com.nesib.countriesapp.ui.quiz.questions.QuestionsFragment
import com.nesib.countriesapp.utils.slideUpDownAnimationNavOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : BaseFragment<FragmentQuizBinding, QuizState, QuizViewModel, QuizFragment.Params>() {

    override val viewModel: QuizViewModel
        get() = ViewModelProvider(this)[QuizViewModel::class.java]

    data class Params(val sample: String? = null) : ScreenParams

    private val quizAdapter by lazy {
        QuizAdapter {
            navigate(R.id.questionsFragment, QuestionsFragment.Params(it.quizType), slideUpDownAnimationNavOptions)
        }
    }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentQuizBinding.inflate(inflater, container, false)

    override fun initViews() = with(binding) {
        showBottomNav(true)
        viewModel.getQuizzes()
        recyclerView.adapter = quizAdapter
    }

    override fun render(state: QuizState) {
        if (!state.loading) {
            quizAdapter.submitItems(state.quizzes)
        }

    }
}