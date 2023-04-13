package com.nesib.countriesapp.ui.quiz.score

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nesib.countriesapp.R
import com.nesib.countriesapp.base.BaseFragment
import com.nesib.countriesapp.base.ScreenParams
import com.nesib.countriesapp.databinding.FragmentScoreBinding
import com.nesib.countriesapp.databinding.FragmentSearchBinding
import com.nesib.countriesapp.models.QuizType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScoreFragment : BaseFragment<FragmentScoreBinding, ScoreState, ScoreViewModel, ScoreFragment.Params>() {

    override val viewModel: ScoreViewModel
        get() = ViewModelProvider(this)[ScoreViewModel::class.java]


    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentScoreBinding.inflate(inflater, container, false)

    override fun initViews(): Unit = with(binding) {
        makeFragmentFullScreen()
        changeStatusBarIconColor(iconsIsLight = true)

        viewModel.getBestScore(params?.quizType)
        exitButton.setOnClickListener {
            findNavController().popBackStack(R.id.navigation_quiz, false)
        }

        params?.let {
            scorePercentage.text =
                ((it.rightCount.toFloat() / it.totalQuestionCount.toFloat()) * 100f).toInt().toString() + "%"
            quizResult.text = HtmlCompat.fromHtml(
                getString(R.string.quiz_result_text, it.totalQuestionCount, it.rightCount),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

        }
    }

    override fun render(state: ScoreState) = with(binding) {
        bestScore.isVisible = state.hasError.not()

        if (state.loading.not() && state.hasError.not()) {
            bestScore.text = getString(R.string.your_best_score_text, state.bestScore)

            if ((params?.rightCount ?: 0) > state.bestScore) {
                viewModel.saveBestScore(params?.quizType, params?.rightCount)
            }
        }

    }

    data class Params(val totalQuestionCount: Int, val rightCount: Int, val quizType: QuizType?) : ScreenParams

}