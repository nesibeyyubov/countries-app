package com.nesib.countriesapp.ui.quiz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nesib.countriesapp.R;

public class QuestionFragment extends Fragment implements View.OnClickListener {
    private Button nextButton;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextButton = view.findViewById(R.id.nextButton);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BottomNavigationView navView =  getActivity().findViewById(R.id.nav_view);
        navView.setVisibility(View.GONE);
        nextButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        QuestionFragmentDirections.ActionQuestionFragmentToScoreFragment action =
                QuestionFragmentDirections.actionQuestionFragmentToScoreFragment();
        navController.navigate(action);
    }
}