package com.nesib.countriesapp.ui.quiz.questions

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import com.nesib.countriesapp.MainActivity
import com.nesib.countriesapp.R
import com.nesib.countriesapp.base.BaseFragment
import com.nesib.countriesapp.base.ScreenParams
import com.nesib.countriesapp.databinding.FragmentQuestionBinding
import com.nesib.countriesapp.models.QuizType
import com.nesib.countriesapp.ui.quiz.score.ScoreFragment
import com.nesib.countriesapp.utils.slideInOutAnimationNavOptions
import com.nesib.countriesapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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

    private val optionContainers by lazy {
        listOf(
            binding.optionA,
            binding.optionB,
            binding.optionC,
            binding.optionD
        )
    }

    private fun resetOptionsUi() {
        optionContainers.forEach {
            it.isClickable = true
            it.setBackgroundResource(R.drawable.option_bg)
        }
        options.forEach {
            it.setTextColor(getColor(R.color.colorTextPrimary))
        }
    }

    override fun onDestroyView() {
        viewModel.cancelTimer()
        super.onDestroyView()
    }


    private fun observeTimer() = with(binding) {
        viewModel.timer
            .onEach {
                countdownText.text = it.toString()
                if (it == 0) {
                    viewModel.saveBestScore(params?.quizType)
                }
            }
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(lifecycleScope)
    }


    override fun onResume() {
        super.onResume()
        changeStatusBarIconColor(iconsShouldBeLight = true)
    }

    override fun onStop() {
        super.onStop()
        changeStatusBarIconColor(iconsShouldBeLight = false)
    }

    override fun initViews() = with(binding) {
        makeFragmentFullScreen()
        showBottomNav(false)
        observeTimer()
        options.forEach { it.isSelected = true }
        viewModel.getQuestions(params?.quizType, requireContext())

        nextButton.setOnClickListener {
            resetOptionsUi()
            viewModel.nextQuestion()
        }

        exitButton.setOnClickListener {
            navigateBack()
        }

        optionContainers.forEachIndexed { index, root ->
            root.setOnClickListener {
                val optionRight = viewModel.checkAnswer(options[index].text.toString())
                val animation = if (optionRight.not()) {
                    root.setBackgroundResource(R.drawable.option_bg_false)
                    AnimationUtils.loadAnimation(requireContext(), R.anim.shake_anim)
                } else {
                    root.setBackgroundResource(R.drawable.option_bg_true)
                    AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_out)
                }
                root.startAnimation(animation)

                options[index].setTextColor(Color.WHITE)

                if (!optionRight) {
                    viewModel.getRightAnswerIndex()?.let {
                        optionContainers[it].setBackgroundResource(R.drawable.option_bg_true)
                        options[it].setTextColor(Color.WHITE)
                    }
                }
                toggleNextBtn(enabled = true)

                optionContainers.forEach { it.isClickable = false }
            }
        }


        when (params?.quizType) {
            QuizType.Regions -> {
                questionImage.isVisible = false
            }
            QuizType.Flags -> {
                questionText.isVisible = false
            }
            QuizType.Capitals -> {
                questionImage.isVisible = false
            }
            null -> {}
        }
    }

    private fun toggleNextBtn(enabled: Boolean) {
        binding.nextButton.isEnabled = enabled
        binding.nextButton.setBackgroundResource(if (enabled) R.drawable.enabled_btn_bg else R.drawable.disabled_btn_bg)
    }

    private fun navigateToScoreFragment() {
        navigate(
            R.id.scoreFragment,
            ScoreFragment.Params(
                viewModel.currentState().currentQuestionNumber,
                viewModel.getRightAnswerCount(),
                params?.quizType
            ),
            slideInOutAnimationNavOptions
        )
    }

    override fun render(state: QuestionsState): Unit = with(binding) {

        if (state.error != null) {
            (requireActivity() as MainActivity)
                .showCustomSnackbar(getString(state.error), Color.WHITE, getColor(R.color.warning_color))
            return@with
        }

        if (state.readyForNavigatingToScoreFragment) {
            navigateToScoreFragment()
            return@with
        }

        questionNumber.isInvisible = state.currentQuestionNumber == 0
        questionNumber.text =
            if (state.loading) getString(R.string.loading_questions_text) else "#${state.currentQuestionNumber}"
        loadingAnimation.isVisible = state.loading

        if (state.questions.isNotEmpty() && state.currentQuestion == null) {
            questionText.isVisible = true
            questionText.text = getString(R.string.click_start_btn_text)
            toggleNextBtn(true)
        }

        nextButton.text =
            if (state.currentQuestionNumber > 0) getString(R.string.next_button_text) else getString(R.string.start_button_text)

        state.currentQuestion?.let { currentQuestion ->
            if (state.loading.not()) {
                questionText.text = currentQuestion.title

                if (params?.quizType == QuizType.Flags) {
                    questionText.isVisible = false
                    questionImage.isVisible = true

                    questionImage.load(currentQuestion.title) {
                        listener(
                            onSuccess = { _, _ ->
                                shimmerFlagImage.hideShimmer()
                            },
                            onStart = {
                                shimmerFlagImage.showShimmer(true)
                            },
                            onError = { _, _ ->
                                shimmerFlagImage.hideShimmer()
                                toast(getString(R.string.error_image_load))
                                toggleNextBtn(enabled = true)
                            }
                        )
                    }
                }

                options.forEachIndexed { index, optionTextView ->
                    optionTextView.text = currentQuestion.options[index]
                }
                optionsContainer.visible()

                toggleNextBtn(enabled = false)
            }

        }

    }


    data class Params(val quizType: QuizType) : ScreenParams

}