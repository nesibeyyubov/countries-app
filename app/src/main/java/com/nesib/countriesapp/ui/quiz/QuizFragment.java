package com.nesib.countriesapp.ui.quiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nesib.countriesapp.Constants;
import com.nesib.countriesapp.R;
import com.nesib.countriesapp.adapters.QuizAdapter;
import com.nesib.countriesapp.models.Quiz;

import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment {
    private QuizAdapter quizAdapter;
    private RecyclerView recyclerView;
    private List<Quiz> quizList;
    private NavController navController;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        quizList = new ArrayList<>();
        navController = Navigation.findNavController(view);
        preferences = getActivity().getSharedPreferences("score_prefs", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        if (!navView.isShown()) {
            navView.setVisibility(View.VISIBLE);
        }
        setupUi();
    }

    public void setupUi() {
        for (int i = 0; i < 3; i++) {
            Quiz quiz = new Quiz();
            if (i == 0) {
                quiz.setQuizType(Constants.QUIZ_TYPE_CAPITALS);
                quiz.setSubTitle(getString(R.string.quiz_subtitle_capital_text));
                quiz.setQuestionCount(500);
                quiz.setTitle(getString(R.string.quiz_title_capital_text));
                quiz.setBestScore(preferences.getInt(Constants.SCORE_KEY_CAPITALS, 0));
            } else if (i == 1) {
                quiz.setQuizType(Constants.QUIZ_TYPE_FLAGS);
                quiz.setBestScore(preferences.getInt(Constants.SCORE_KEY_FLAGS, 0));
                quiz.setSubTitle(getString(R.string.quiz_subtitle_flags_text));
                quiz.setQuestionCount(500);
                quiz.setTitle(getString(R.string.quiz_title_flags_text));

            } else {
                quiz.setQuizType(Constants.QUIZ_TYPE_REGION);
                quiz.setBestScore(preferences.getInt(Constants.SCORE_KEY_REGIONS, 0));
                quiz.setSubTitle(getString(R.string.quiz_subtitle_regions_text));
                quiz.setQuestionCount(500);
                quiz.setTitle(getString(R.string.quiz_title_regions_text));
            }
            quizList.add(quiz);
        }

        quizAdapter = new QuizAdapter(quizList, getActivity());
        quizAdapter.setOnItemClickListener(new QuizAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                QuizFragmentDirections.ActionNavigationQuizToQuestionFragment action =
                        QuizFragmentDirections.actionNavigationQuizToQuestionFragment();
                action.setQuizType(quizList.get(position).getQuizType());
                navController.navigate(action);

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(quizAdapter);
    }

}