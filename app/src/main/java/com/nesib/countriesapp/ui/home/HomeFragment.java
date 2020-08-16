package com.nesib.countriesapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavAction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;
import com.nesib.countriesapp.R;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private TextView nameTextView;
    private String name;
    private RelativeLayout europa,asia,africa,oceania,america;
    private NavController navController;
    private HomeFragmentDirections.ActionNavigationHomeToResultsFragment action;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameTextView = view.findViewById(R.id.nameTextView);
        europa = view.findViewById(R.id.europa);
        asia = view.findViewById(R.id.asia);
        africa = view.findViewById(R.id.africa);
        oceania = view.findViewById(R.id.oceania);
        america = view.findViewById(R.id.america);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupName();
        setupClickListeners();

        action = HomeFragmentDirections.actionNavigationHomeToResultsFragment();
    }

    private void setupName(){
        name = getActivity().getIntent().getStringExtra("name");
        name = "Hi, " + name;
        nameTextView.setText(name);
    }
    private void setupClickListeners(){
        europa.setOnClickListener(this);
        asia.setOnClickListener(this);
        africa.setOnClickListener(this);
        oceania.setOnClickListener(this);
        america.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.europa:
                action.setRegionName("Europe");
                break;
            case R.id.asia:
                action.setRegionName("Asia");
                break;
            case R.id.africa:
                action.setRegionName("Africa");
                break;
            case R.id.america:
                action.setRegionName("Americas");
                break;
            case R.id.oceania:
                action.setRegionName("Oceania");
                break;
        }
        navController.navigate(action);
    }

}