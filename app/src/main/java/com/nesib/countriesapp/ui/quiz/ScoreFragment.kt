package com.nesib.countriesapp.ui.quiz

import android.content.Context
import android.widget.TextView
import androidx.navigation.NavController
import android.content.SharedPreferences
import android.os.Bundle
import com.nesib.countriesapp.R
import android.text.Html
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.nesib.countriesapp.Constants

class ScoreFragment : Fragment(R.layout.fragment_score) {
    private lateinit var scorePercentage: TextView
    private lateinit var bestScore: TextView
    private lateinit var quizResult: TextView
    private lateinit var exitButton: TextView
    private lateinit var navController: NavController
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var bestScoreValue = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scorePercentage = view.findViewById(R.id.scorePercentage)
        bestScore = view.findViewById(R.id.bestScore)
        quizResult = view.findViewById(R.id.quizResult)
        exitButton = view.findViewById(R.id.exitButton)
        navController = Navigation.findNavController(view)
        preferences = requireActivity().getSharedPreferences("score_prefs", Context.MODE_PRIVATE)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        assert(arguments != null)
        when (ScoreFragmentArgs.fromBundle(requireArguments()).quizType) {
            Constants.QUIZ_TYPE_FLAGS -> {
                bestScoreValue = preferences.getInt(Constants.SCORE_KEY_FLAGS, 0)
                setupScoreUi(Constants.SCORE_KEY_FLAGS)
            }
            Constants.QUIZ_TYPE_CAPITALS -> {
                bestScoreValue = preferences.getInt("bestScoreCapitals", 0)
                setupScoreUi(Constants.SCORE_KEY_CAPITALS)
            }
            Constants.QUIZ_TYPE_REGION -> {
                bestScoreValue = preferences.getInt("bestScoreRegions", 0)
                setupScoreUi(Constants.SCORE_KEY_REGIONS)
            }
        }
        exitButton.setOnClickListener {
            navController.popBackStack(
                R.id.navigation_quiz,
                false
            )
        }
    }

    private fun setupScoreUi(preferencesKey: String?) {
        val correctCount = ScoreFragmentArgs.fromBundle(requireArguments()).correctCount
        val questionCount = ScoreFragmentArgs.fromBundle(requireArguments()).questionCount
        scorePercentage.text =
            (correctCount * 100 / questionCount).toString() + "% " + getString(R.string.score_text)
        val quizResultText =
            Html.fromHtml(getString(R.string.quiz_result_text, questionCount, correctCount))
        quizResult.text = quizResultText
        bestScore.text = getString(R.string.your_best_score_text) + bestScoreValue
        editor = preferences.edit()
        if (bestScoreValue < correctCount) {
            editor.putInt(preferencesKey, correctCount)
            editor.apply()
            bestScore.text = getString(R.string.your_best_score_text) + correctCount
        }
    }
}