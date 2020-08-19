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

import com.nesib.countriesapp.R;

public class ScoreFragment extends Fragment {
    private TextView scorePercentage,bestScore,quizResult,exitButton;
    private NavController navController;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

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
        int bestScoreValue = preferences.getInt("bestScore",0);

        int correctCount = ScoreFragmentArgs.fromBundle(getArguments()).getCorrectCount();
        int questionCount = ScoreFragmentArgs.fromBundle(getArguments()).getQuestionCount();

        scorePercentage.setText((correctCount*100)/questionCount + "% Score");
        Spanned quizResultText = Html.fromHtml("You attempted <b>"+questionCount+" questions</b> <br/> from that <b>"+correctCount+" answer</b> is right");
        quizResult.setText(quizResultText);
        bestScore.setText("Your best score is: "+bestScoreValue);

        editor = preferences.edit();
        if(bestScoreValue < correctCount){
            editor.putInt("bestScore",correctCount);
            editor.apply();
            bestScore.setText("Your best score is: "+correctCount);
        }

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_scoreFragment_to_navigation_quiz);
            }
        });
    }
}