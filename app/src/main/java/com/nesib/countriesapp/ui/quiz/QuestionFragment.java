package com.nesib.countriesapp.ui.quiz;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nesib.countriesapp.R;
import com.nesib.countriesapp.models.Country;
import com.nesib.countriesapp.viewmodels.CountriesViewModel;

import java.util.ArrayList;
import java.util.List;

public class QuestionFragment extends Fragment implements View.OnClickListener {
    private Button nextButton;
    private NavController navController;
    private CountriesViewModel countriesViewModel;
    private TextView questionNumber,questionText,countdownText,option_a_text,option_b_text,option_c_text,option_d_text,exitButton;
    private LottieAnimationView loadingAnimation;
    private ScrollView optionsContainer;
    private LinearLayout option_a,option_b,option_c,option_d;
    private ImageView questionImage;

    // Member variables
    private List<TextView> optionTextViewList;
    private List<Country> allCountries;
    private List<Country> options;
    private List<LinearLayout> optionContainerList;
    private String quizType;
    private boolean quizStarted = false;
    private Country answer;
    private int questionNumberValue = 0;
    private int correctCount = 0;
    public static final String TAG = "mytag";

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
        questionNumber = view.findViewById(R.id.questionNumber);
        questionText = view.findViewById(R.id.questionText);
        countdownText = view.findViewById(R.id.countdownText);
        option_a_text = view.findViewById(R.id.option_a_text);
        option_b_text = view.findViewById(R.id.option_b_text);
        option_c_text = view.findViewById(R.id.option_c_text);
        option_d_text = view.findViewById(R.id.option_d_text);
        option_a = view.findViewById(R.id.option_a);
        option_b = view.findViewById(R.id.option_b);
        option_c = view.findViewById(R.id.option_c);
        option_d = view.findViewById(R.id.option_d);
        questionImage = view.findViewById(R.id.questionImage);
        loadingAnimation = view.findViewById(R.id.loadingAnimation);
        optionsContainer = view.findViewById(R.id.optionsContainer);
        exitButton = view.findViewById(R.id.exitButton);

        navController = Navigation.findNavController(view);
        countriesViewModel = new ViewModelProvider(getActivity()).get(CountriesViewModel.class);
        optionTextViewList = new ArrayList<>();
        optionContainerList = new ArrayList<>();
        optionTextViewList.add(option_a_text);
        optionTextViewList.add(option_b_text);
        optionTextViewList.add(option_c_text);
        optionTextViewList.add(option_d_text);


        optionContainerList.add(option_a);optionContainerList.add(option_b);
        optionContainerList.add(option_c);optionContainerList.add(option_d);
        for(LinearLayout optionContainer: optionContainerList){
            optionContainer.setOnClickListener(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupUi();
        fetchAllCountries();

    }

    public void makeQuestion(){
        int min = 0;
        int max = allCountries.size()-2;
        int answerIndex = getRandomIndex(min,max);
        Country answer = allCountries.get(answerIndex);
        allCountries.remove(answerIndex);
        ArrayList<Integer>optionIndexList = new ArrayList<>();
        options = new ArrayList<>();
        optionIndexList.add(answerIndex);
        options.add(answer);
        int optionCount = 0;
        while(optionCount != 3){
            int index = getRandomIndex(min,max);
            if (!optionIndexList.contains(index)){
                optionIndexList.add(index);
                options.add(allCountries.get(index));
                optionCount++;
            }
        }
        setupQuestionUi();
    }

    public void setupQuestionUi(){
        answer = options.get(0);
        switch (quizType){
            case "flags":
                showFlagsQuestion();
                break;
            case "regions":
                break;
            case "capitals":
                break;
        }
    }

    public void showFlagsQuestion(){
        if(questionText.getVisibility() == View.VISIBLE){
            questionText.setVisibility(View.GONE);
        }
        if(optionsContainer.getVisibility() == View.INVISIBLE){
            optionsContainer.setVisibility(View.VISIBLE);
        }
        String flagImageUrl = answer.getFlag();
        for(TextView optionTextView : optionTextViewList){
            int optionIndex = getRandomIndex(0,options.size()-1);
            Country option = options.get(optionIndex);
            optionTextView.setText(option.getName());
            options.remove(optionIndex);
        }
        questionImage.setVisibility(View.VISIBLE);
        GlideToVectorYou
                .init()
                .with(getActivity())
                .load(Uri.parse(flagImageUrl),questionImage);
    }

    public int getRandomIndex(int min,int max){
        return (int)(Math.random() * (max - min + 1 )) + min;
    }

    public void setupUi(){
        quizType = QuestionFragmentArgs.fromBundle(getArguments()).getQuizType();

        BottomNavigationView navView =  getActivity().findViewById(R.id.nav_view);
        navView.setVisibility(View.GONE);
        nextButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);

        countriesViewModel.getIsLoading().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(!isLoading){
                    loadingAnimation.cancelAnimation();
                    loadingAnimation.setVisibility(View.GONE);
                    questionNumber.setText("Question 00");
                    questionText.setVisibility(View.VISIBLE);
                    questionText.setText("Click start button to play the quiz");

                    enableNextButton();
                }
            }
        });
    }

    public void enableNextButton(){
        nextButton.setEnabled(true);
        nextButton.setBackground(getResources().getDrawable(R.drawable.option_bg_true));
    }

    public void disableNextButton(){
        nextButton.setEnabled(false);
        nextButton.setBackground(getResources().getDrawable(R.drawable.disabled_btn_bg));
    }

    public void fetchAllCountries(){
        countriesViewModel.getAllCountries().observe(getActivity(), new Observer<List<Country>>() {
            @Override
            public void onChanged(List<Country> countries) {
                if(countries != null){
                    allCountries = countries;
                }
                else{
                    Log.d(TAG, "onChanged: Countries list is null");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nextButton:
                if(nextButton.getText().toString().equals("Start")){
                    quizStarted = true;
                    nextButton.setText("Next");
                    disableNextButton();
                    new CountDownTimer(20000,1000) {
                        @Override
                        public void onTick(long millis) {
                            String text = (int)(millis/1000) + "";
                            countdownText.setText(text);
                        }

                        @Override
                        public void onFinish() {
                            QuestionFragmentDirections.ActionQuestionFragmentToScoreFragment action =
                                    QuestionFragmentDirections.actionQuestionFragmentToScoreFragment();
                            action.setCorrectCount(correctCount);
                            action.setQuestionCount(questionNumberValue);
                            navController.navigate(action);
                        }
                    }.start();
                }
                questionNumberValue++;
                resetOptions();
                makeQuestion();
                String questionNumberText = questionNumberValue/10 == 0 ? "Question 0"+questionNumberValue : "Question "+questionNumberValue;
                questionNumber.setText(questionNumberText);
                break;
            case R.id.exitButton:
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
                break;
            case R.id.option_a:
                checkAnswer(option_a_text,option_a);
                break;
            case R.id.option_b:
                checkAnswer(option_b_text,option_b);
                break;
            case R.id.option_c:
                checkAnswer(option_c_text,option_c);
                break;
            case R.id.option_d:
                checkAnswer(option_d_text,option_d);
                break;
        }
    }

    public void checkAnswer(TextView optionTextView,LinearLayout optionContainer){
        if(answer.getName().equals(optionTextView.getText().toString())){
            Animation scaleInOutAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_in_out);
            optionContainer.setBackground(getResources().getDrawable(R.drawable.option_bg_true));
            optionContainer.startAnimation(scaleInOutAnimation);
            correctCount++;
        }
        else{
            optionContainer.setBackground(getResources().getDrawable(R.drawable.option_bg_false));
            Animation shakeAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.shake_anim);
            optionContainer.startAnimation(shakeAnimation);
        }
        for(LinearLayout opt: optionContainerList){
            opt.setEnabled(false);
        }
        optionTextView.setTextColor(Color.WHITE);
        enableNextButton();
    }

    public void resetOptions(){
        Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.scale_in_from_zero);
        for(LinearLayout optionContainer: optionContainerList){
            optionContainer.setBackground(getResources().getDrawable(R.drawable.option_bg));
            optionContainer.startAnimation(scaleAnimation);
            optionContainer.setEnabled(true);
        }
        for(TextView optionTextView:optionTextViewList){
            optionTextView.setTextColor(getResources().getColor(R.color.colorTextPrimary));
        }
        disableNextButton();
    }
}