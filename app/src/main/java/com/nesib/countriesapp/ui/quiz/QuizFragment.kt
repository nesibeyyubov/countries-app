package com.nesib.countriesapp.ui.quiz

import android.content.Context
import com.nesib.countriesapp.adapters.QuizAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nesib.countriesapp.models.Quiz
import androidx.navigation.NavController
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.nesib.countriesapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.recyclerview.widget.LinearLayoutManager
import com.nesib.countriesapp.Constants
import java.util.ArrayList

class QuizFragment : Fragment(R.layout.fragment_quiz) {
    private lateinit var quizAdapter: QuizAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var navController: NavController
    private lateinit var preferences: SharedPreferences

    private var quizList = mutableListOf<Quiz>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        quizList = ArrayList()
        navController = Navigation.findNavController(view)
        preferences = requireActivity().getSharedPreferences("score_prefs", Context.MODE_PRIVATE)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        if (!navView.isShown) {
            navView.visibility = View.VISIBLE
        }
        setupUi()
    }

    private fun setupUi() {
        for (i in 0..2) {
            val quiz = Quiz()
            if (i == 0) {
                quiz.quizType = Constants.QUIZ_TYPE_CAPITALS
                quiz.subTitle = getString(R.string.quiz_subtitle_capital_text)
                quiz.questionCount = 500
                quiz.title = getString(R.string.quiz_title_capital_text)
                quiz.bestScore = preferences.getInt(Constants.SCORE_KEY_CAPITALS, 0)
            } else if (i == 1) {
                quiz.quizType = Constants.QUIZ_TYPE_FLAGS
                quiz.bestScore = preferences.getInt(Constants.SCORE_KEY_FLAGS, 0)
                quiz.subTitle = getString(R.string.quiz_subtitle_flags_text)
                quiz.questionCount = 500
                quiz.title = getString(R.string.quiz_title_flags_text)
            } else {
                quiz.quizType = Constants.QUIZ_TYPE_REGION
                quiz.bestScore = preferences.getInt(Constants.SCORE_KEY_REGIONS, 0)
                quiz.subTitle = getString(R.string.quiz_subtitle_regions_text)
                quiz.questionCount = 500
                quiz.title = getString(R.string.quiz_title_regions_text)
            }
            quizList.add(quiz)
        }
        quizAdapter = QuizAdapter(quizList, requireActivity())
        quizAdapter.setOnItemClickListener(object : QuizAdapter.OnItemClickListener {
            override fun onClick(position: Int) {
                val action = QuizFragmentDirections.actionNavigationQuizToQuestionFragment()
                action.quizType = quizList[position].quizType
                navController.navigate(action)
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = quizAdapter
    }
}