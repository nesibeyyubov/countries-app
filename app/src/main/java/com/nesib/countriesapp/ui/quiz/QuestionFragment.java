package com.nesib.countriesapp.ui.quiz;

import android.app.AlertDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
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
import com.nesib.countriesapp.Constants;
import com.nesib.countriesapp.R;
import com.nesib.countriesapp.models.Country;
import com.nesib.countriesapp.viewmodels.CountriesViewModel;

import java.util.ArrayList;
import java.util.List;

public class QuestionFragment extends Fragment implements View.OnClickListener {
    private Button nextButton;
    private NavController navController;
    private CountriesViewModel countriesViewModel;
    private TextView questionNumber, questionText, countdownText, option_a_text, option_b_text, option_c_text, option_d_text, exitButton;
    private LottieAnimationView loadingAnimation;
    private ScrollView optionsContainer;
    private LinearLayout option_a, option_b, option_c, option_d;
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
    private CountDownTimer countDownTimer;
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


        optionContainerList.add(option_a);
        optionContainerList.add(option_b);
        optionContainerList.add(option_c);
        optionContainerList.add(option_d);
        for (LinearLayout optionContainer : optionContainerList) {
            optionContainer.setOnClickListener(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupUi();
        fetchAllCountries();
        countriesViewModel.getHasFailure().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean hasFailure) {
                if(hasFailure){
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    questionText.setText("");
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.error_box,null,false);
                    dialogBuilder.setView(view);
                    Button okButton = view.findViewById(R.id.okButton);
                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            navController.popBackStack();
                            dialog.dismiss();
                        }
                    });

                }
            }
        });
    }


    public void makeQuestion() {
        int min = 0;
        int max = allCountries.size() - 2;
        int answerIndex = getRandomIndex(min, max);
        Country answer = allCountries.get(answerIndex);
        allCountries.remove(answerIndex);
        ArrayList<Integer> optionIndexList = new ArrayList<>();
        options = new ArrayList<>();
        optionIndexList.add(answerIndex);
        options.add(answer);
        int optionCount = 0;
        while (optionCount != 3) {
            int index = getRandomIndex(min, max);
            if (!optionIndexList.contains(index)) {
                optionIndexList.add(index);
                options.add(allCountries.get(index));
                optionCount++;
            }
        }
        setupQuestionUi();
    }

    public void setupQuestionUi() {
        answer = options.get(0);
        switch (quizType) {
            case Constants
                    .QUIZ_TYPE_FLAGS:
                showFlagsQuestion();
                break;
            case Constants
                    .QUIZ_TYPE_REGION:
                showRegionsQuestion();
                break;
            case Constants
                    .QUIZ_TYPE_CAPITALS:
                showCapitalsQuestions();
                break;
        }
    }

    public void showFlagsQuestion() {
        if (questionText.getVisibility() == View.VISIBLE) {
            questionText.setVisibility(View.GONE);
        }
        if (optionsContainer.getVisibility() == View.INVISIBLE) {
            optionsContainer.setVisibility(View.VISIBLE);
        }
        String flagImageUrl = answer.getFlag();
        for (TextView optionTextView : optionTextViewList) {
            int optionIndex = getRandomIndex(0, options.size() - 1);
            Country option = options.get(optionIndex);
            optionTextView.setText(option.getName());
            options.remove(optionIndex);
        }
        questionImage.setVisibility(View.VISIBLE);
        GlideToVectorYou
                .init()
                .with(getActivity())
                .load(Uri.parse(flagImageUrl), questionImage);
    }

    public void showRegionsQuestion() {
        if (optionsContainer.getVisibility() == View.INVISIBLE) {
            optionsContainer.setVisibility(View.VISIBLE);
        }
        String region = answer.getRegion();
        for (TextView optionTextView : optionTextViewList) {
            int optionIndex = getRandomIndex(0, options.size() - 1);
            Country option = options.get(optionIndex);
            optionTextView.setText(option.getName());
            options.remove(optionIndex);
        }
        questionText.setText(getString(R.string.question_region_text)+region);
    }

    public void showCapitalsQuestions() {
        if (optionsContainer.getVisibility() == View.INVISIBLE) {
            optionsContainer.setVisibility(View.VISIBLE);
        }
        String capital = answer.getCapital();
        for (TextView optionTextView : optionTextViewList) {
            int optionIndex = getRandomIndex(0, options.size() - 1);
            Country option = options.get(optionIndex);
            optionTextView.setText(option.getName());
            options.remove(optionIndex);
        }
        questionText.setText(capital + " "+ getString(R.string.question_capital_text));
    }

    public int getRandomIndex(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public void setupUi() {
        quizType = QuestionFragmentArgs.fromBundle(getArguments()).getQuizType();

        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        navView.setVisibility(View.GONE);
        nextButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);

        countriesViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (!isLoading) {
                    loadingAnimation.cancelAnimation();
                    loadingAnimation.setVisibility(View.GONE);
                    questionNumber.setText(getString(R.string.question_number_text));
                    questionText.setVisibility(View.VISIBLE);
                    questionText.setText(getString(R.string.click_start_btn_text));

                    enableNextButton();
                }
            }
        });
    }

    public void enableNextButton() {
        nextButton.setEnabled(true);
        nextButton.setBackground(getResources().getDrawable(R.drawable.option_bg_true));
    }

    public void disableNextButton() {
        nextButton.setEnabled(false);
        nextButton.setBackground(getResources().getDrawable(R.drawable.disabled_btn_bg));
    }

    public void fetchAllCountries() {
        countriesViewModel.getAllCountries().observe(getActivity(), new Observer<List<Country>>() {
            @Override
            public void onChanged(List<Country> countries) {
                if (countries != null) {
                    allCountries = countries;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton:
                if (nextButton.getText().toString().equals(getString(R.string.start_button_text))) {
                    quizStarted = true;
                    nextButton.setText(getString(R.string.next_button_text));
                    disableNextButton();
                    countDownTimer = new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millis) {
                            String text = (int) (millis / 1000) + "";
                            countdownText.setText(text);
                        }

                        @Override
                        public void onFinish() {
                            QuestionFragmentDirections.ActionQuestionFragmentToScoreFragment action =
                                    QuestionFragmentDirections.actionQuestionFragmentToScoreFragment();
                            action.setCorrectCount(correctCount);
                            action.setQuestionCount(questionNumberValue);
                            action.setQuizType(quizType);
                            navController.navigate(action);
                        }
                    };
                    countDownTimer.start();
                }
                questionNumberValue++;
                resetOptions();
                makeQuestion();
                String questionNumberText = questionNumberValue / 10 == 0 ? getString(R.string.question_number_text)+" 0" + questionNumberValue : getString(R.string.question_number_text) +" "+ questionNumberValue;
                questionNumber.setText(questionNumberText);
                break;
            case R.id.exitButton:
                if(quizStarted){
                    countDownTimer.cancel();
                }
                navController.popBackStack();
                break;
            case R.id.option_a:
                checkAnswer(option_a_text, option_a);
                break;
            case R.id.option_b:
                checkAnswer(option_b_text, option_b);
                break;
            case R.id.option_c:
                checkAnswer(option_c_text, option_c);
                break;
            case R.id.option_d:
                checkAnswer(option_d_text, option_d);
                break;
        }
    }

    public void checkAnswer(TextView optionTextView, LinearLayout optionContainer) {
        if (answer.getName().equals(optionTextView.getText().toString())) {
            Animation scaleInOutAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_out);
            optionContainer.setBackground(getResources().getDrawable(R.drawable.option_bg_true));
            optionContainer.startAnimation(scaleInOutAnimation);
            correctCount++;
        } else {
            optionContainer.setBackground(getResources().getDrawable(R.drawable.option_bg_false));
            Animation shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_anim);
            optionContainer.startAnimation(shakeAnimation);
        }
        for (LinearLayout opt : optionContainerList) {
            opt.setEnabled(false);
        }
        optionTextView.setTextColor(Color.WHITE);
        enableNextButton();
    }

    public void resetOptions() {
        Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_in_from_zero);
        for (LinearLayout optionContainer : optionContainerList) {
            optionContainer.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.option_bg,null));
            optionContainer.startAnimation(scaleAnimation);
            optionContainer.setEnabled(true);
        }
        for (TextView optionTextView : optionTextViewList) {
            optionTextView.setTextColor(getResources().getColor(R.color.colorTextPrimary));
        }
        disableNextButton();
    }
}