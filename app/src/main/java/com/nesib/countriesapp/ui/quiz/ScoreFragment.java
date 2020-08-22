package com.nesib.countriesapp.ui.quiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nesib.countriesapp.Constants;
import com.nesib.countriesapp.R;

public class ScoreFragment extends Fragment {
    private TextView scorePercentage, bestScore, quizResult, exitButton;
    private NavController navController;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private int bestScoreValue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scorePercentage = view.findViewById(R.id.scorePercentage);
        bestScore = view.findViewById(R.id.bestScore);
        quizResult = view.findViewById(R.id.quizResult);
        exitButton = view.findViewById(R.id.exitButton);

        navController = Navigation.findNavController(view);
        preferences = getActivity().getSharedPreferences("score_prefs", Context.MODE_PRIVATE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assert getArguments() != null;
        switch (ScoreFragmentArgs.fromBundle(getArguments()).getQuizType()) {
            case Constants
                    .QUIZ_TYPE_FLAGS:
                bestScoreValue = preferences.getInt(Constants.SCORE_KEY_FLAGS, 0);
                setupScoreUi(Constants.SCORE_KEY_FLAGS);
                break;
            case Constants
                    .QUIZ_TYPE_CAPITALS:
                bestScoreValue = preferences.getInt("bestScoreCapitals", 0);
                setupScoreUi(Constants.SCORE_KEY_CAPITALS);
                break;
            case Constants
                    .QUIZ_TYPE_REGION:
                bestScoreValue = preferences.getInt("bestScoreRegions", 0);
                setupScoreUi(Constants.SCORE_KEY_REGIONS);
                break;
        }


        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack(R.id.navigation_quiz, false);
            }
        });
    }

    public void setupScoreUi(String preferencesKey) {
        int correctCount = ScoreFragmentArgs.fromBundle(getArguments()).getCorrectCount();
        int questionCount = ScoreFragmentArgs.fromBundle(getArguments()).getQuestionCount();

        scorePercentage.setText((correctCount * 100) / questionCount + "% " + getString(R.string.score_text));
        Spanned quizResultText = Html.fromHtml(getString(R.string.quiz_result_text, questionCount, correctCount));
        quizResult.setText(quizResultText);

        bestScore.setText(getString(R.string.your_best_score_text) + bestScoreValue);

        editor = preferences.edit();
        if (bestScoreValue < correctCount) {
            editor.putInt(preferencesKey, correctCount);
            editor.apply();
            bestScore.setText(getString(R.string.your_best_score_text) + correctCount);
        }


    }
}