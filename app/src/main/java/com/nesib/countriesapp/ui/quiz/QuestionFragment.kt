package com.nesib.countriesapp.ui.quiz

import android.graphics.Color
import android.net.Uri
import androidx.navigation.NavController
import com.airbnb.lottie.LottieAnimationView
import com.nesib.countriesapp.models.Country
import android.os.CountDownTimer
import android.os.Bundle
import android.view.View
import com.nesib.countriesapp.R
import androidx.lifecycle.ViewModelProvider
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import androidx.core.content.res.ResourcesCompat
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.nesib.countriesapp.utils.Constants
import com.nesib.countriesapp.viewmodels.CountriesViewModel
import java.util.ArrayList

class QuestionFragment : Fragment(R.layout.fragment_question), View.OnClickListener {
    private lateinit var nextButton: Button
    private lateinit var navController: NavController
    private lateinit var countriesViewModel: CountriesViewModel
    private lateinit var questionNumber: TextView
    private lateinit var questionText: TextView
    private lateinit var countdownText: TextView
    private lateinit var option_a_text: TextView
    private lateinit var option_b_text: TextView
    private lateinit var option_c_text: TextView
    private lateinit var option_d_text: TextView
    private lateinit var exitButton: TextView
    private lateinit var loadingAnimation: LottieAnimationView
    private lateinit var optionsContainer: ScrollView
    private lateinit var option_a: LinearLayout
    private lateinit var option_b: LinearLayout
    private lateinit var option_c: LinearLayout
    private lateinit var option_d: LinearLayout
    private lateinit var questionImage: ImageView
    private var answerOptionContainer: LinearLayout? = null


    // Member variables
    private lateinit var optionTextViewList: MutableList<TextView>
    private var allCountries: MutableList<Country>? = null
    private var options = mutableListOf<Country>()
    private lateinit var optionContainerList: MutableList<LinearLayout>
    private var quizType: String? = null
    private var quizStarted = false
    private var answer: Country? = null
    private var questionNumberValue = 0
    private var correctCount = 0
    private lateinit var countDownTimer: CountDownTimer


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nextButton = view.findViewById(R.id.nextButton)
        questionNumber = view.findViewById(R.id.questionNumber)
        questionText = view.findViewById(R.id.questionText)
        countdownText = view.findViewById(R.id.countdownText)
        option_a_text = view.findViewById(R.id.option_a_text)
        option_b_text = view.findViewById(R.id.option_b_text)
        option_c_text = view.findViewById(R.id.option_c_text)
        option_d_text = view.findViewById(R.id.option_d_text)
        option_a = view.findViewById(R.id.option_a)
        option_b = view.findViewById(R.id.option_b)
        option_c = view.findViewById(R.id.option_c)
        option_d = view.findViewById(R.id.option_d)
        questionImage = view.findViewById(R.id.questionImage)
        loadingAnimation = view.findViewById(R.id.loadingAnimation)
        optionsContainer = view.findViewById(R.id.optionsContainer)
        exitButton = view.findViewById(R.id.exitButton)
        navController = Navigation.findNavController(view)
        countriesViewModel = ViewModelProvider(requireActivity()).get(
            CountriesViewModel::class.java
        )
        optionTextViewList = ArrayList()
        optionContainerList = ArrayList()
        optionTextViewList.add(option_a_text)
        optionTextViewList.add(option_b_text)
        optionTextViewList.add(option_c_text)
        optionTextViewList.add(option_d_text)
        optionContainerList.add(option_a)
        optionContainerList.add(option_b)
        optionContainerList.add(option_c)
        optionContainerList.add(option_d)
        for (optionContainer in optionContainerList) {
            optionContainer.setOnClickListener(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        setupUi()
//        fetchAllCountries()
//        countriesViewModel.hasFailure.observe(viewLifecycleOwner, { hasFailure ->
//            if (hasFailure) {
//                val dialogBuilder = AlertDialog.Builder(context)
//                questionText.text = ""
//                val view = LayoutInflater.from(context).inflate(R.layout.error_box, null, false)
//                dialogBuilder.setView(view)
//                val okButton = view.findViewById<Button>(R.id.okButton)
//                val dialog = dialogBuilder.create()
//                dialog.show()
//                okButton.setOnClickListener {
//                    navController.popBackStack()
//                    dialog.dismiss()
//                }
//            }
//        })
    }

    private fun makeQuestion() {
        if (allCountries!!.isEmpty()) {
            navigateToScoreFragment()
        }
        val min = 0
        val max = allCountries!!.size - 2
        val answerIndex = getRandomIndex(min, max)
        val answer = allCountries!![answerIndex]
        allCountries!!.removeAt(answerIndex)
        val optionIndexList = ArrayList<Int>()
        options = ArrayList()
        optionIndexList.add(answerIndex)
        options.add(answer)
        var optionCount = 0
        while (optionCount != 3) {
            val index = getRandomIndex(min, max)
            if (!optionIndexList.contains(index)) {
                optionIndexList.add(index)
                options.add(allCountries!![index])
                optionCount++
            }
        }
        setupQuestionUi()
    }

    private fun setupQuestionUi() {
        answer = options!![0]
        when (quizType) {
            Constants.QUIZ_TYPE_FLAGS -> showFlagsQuestion()
            Constants.QUIZ_TYPE_REGION -> showRegionsQuestion()
            Constants.QUIZ_TYPE_CAPITALS -> showCapitalsQuestions()
        }
    }

    private fun makeRandomOptions() {
        var index = 0
        for (optionTextView in optionTextViewList) {
            val optionIndex = getRandomIndex(0, options.size - 1)
            val option = options[optionIndex]
            optionTextView.text = option.name
            options.removeAt(optionIndex)
            if (option.name == answer!!.name) {
                answerOptionContainer = optionContainerList[index]
            }
            index++
        }
    }

    private fun showFlagsQuestion() {
        if (questionText.visibility == View.VISIBLE) {
            questionText.visibility = View.GONE
        }
        if (optionsContainer.visibility == View.INVISIBLE) {
            optionsContainer.visibility = View.VISIBLE
        }
        val flagImageUrl = answer!!.flag
        makeRandomOptions()
        questionImage.visibility = View.VISIBLE
        GlideToVectorYou
            .init()
            .with(activity)
            .load(Uri.parse(flagImageUrl), questionImage)
    }

    private fun showRegionsQuestion() {
        if (optionsContainer.visibility == View.INVISIBLE) {
            optionsContainer.visibility = View.VISIBLE
        }
        val region = answer!!.region
        makeRandomOptions()
        questionText.text =
            getString(R.string.question_region_text) + ":  " + "\"" + region + "\""
    }

    private fun showCapitalsQuestions() {
        if (optionsContainer.visibility == View.INVISIBLE) {
            optionsContainer.visibility = View.VISIBLE
        }
        val capital = answer!!.capital
        makeRandomOptions()
        if (capital == null || capital.isEmpty()) {
            questionText.setText(R.string.which_country_not_have_capital)
        } else {
            questionText.text =
                "\"" + capital + "\"" + " " + getString(R.string.question_capital_text)
        }
    }

    private fun getRandomIndex(min: Int, max: Int): Int {
        return (Math.random() * (max - min + 1)).toInt() + min
    }

//    private fun setupUi() {
//        assert(arguments != null)
//        quizType = QuestionFragmentArgs.fromBundle(requireArguments()).quizType
//        val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
//        navView.visibility = View.GONE
//        nextButton.setOnClickListener(this)
//        exitButton.setOnClickListener(this)
//        countriesViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
//            if (!isLoading!!) {
//                loadingAnimation.cancelAnimation()
//                loadingAnimation.visibility = View.GONE
//                questionNumber.text = getString(R.string.question_number_text)
//                questionText.visibility = View.VISIBLE
//                questionText.text = getString(R.string.click_start_btn_text)
//                enableNextButton()
//            }
//        })
//    }

    private fun enableNextButton() {
        nextButton.isEnabled = true
        nextButton.background =
            ResourcesCompat.getDrawable(resources, R.drawable.option_bg_true, null)
    }

    private fun disableNextButton() {
        nextButton.isEnabled = false
        nextButton.background =
            ResourcesCompat.getDrawable(resources, R.drawable.disabled_btn_bg, null)
    }

//    private fun fetchAllCountries() {
//        countriesViewModel.allCountries.observe(viewLifecycleOwner, { countries ->
//            if (countries != null) {
//                allCountries = countries.toMutableList()
//            }
//        })
//    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.nextButton -> {
                if (nextButton.text.toString() == getString(R.string.start_button_text)) {
                    quizStarted = true
                    nextButton.text = getString(R.string.next_button_text)
                    disableNextButton()
                    countDownTimer = object : CountDownTimer(Constants.QUIZ_DURATION, 1000) {
                        override fun onTick(millis: Long) {
                            val text: String = (millis / 1000).toString()
                            countdownText.text = text
                        }

                        override fun onFinish() {
                            navigateToScoreFragment()
                        }
                    }
                    countDownTimer.start()
                }
                questionNumberValue++
                resetOptions()
                makeQuestion()
                val questionNumberText =
                    if (questionNumberValue / 10 == 0) getString(R.string.question_number_text) + " 0" + questionNumberValue else getString(
                        R.string.question_number_text
                    ) + " " + questionNumberValue
                questionNumber.text = questionNumberText
            }
            R.id.exitButton -> {
                if (quizStarted) {
                    countDownTimer!!.cancel()
                }
                navController.popBackStack()
            }
            R.id.option_a -> checkAnswer(option_a_text, option_a)
            R.id.option_b -> checkAnswer(option_b_text, option_b)
            R.id.option_c -> checkAnswer(option_c_text, option_c)
            R.id.option_d -> checkAnswer(option_d_text, option_d)
        }
    }

    fun navigateToScoreFragment() {
        val action = QuestionFragmentDirections.actionQuestionFragmentToScoreFragment()
        action.correctCount = correctCount
        action.questionCount = questionNumberValue
        action.quizType = quizType!!
        navController.navigate(action)
    }

    private fun checkAnswer(optionTextView: TextView?, optionContainer: LinearLayout?) {
        if (answer!!.name == optionTextView!!.text.toString()) {
            val scaleInOutAnimation = AnimationUtils.loadAnimation(
                activity, R.anim.fade_in_out
            )
            optionContainer!!.background =
                ResourcesCompat.getDrawable(resources, R.drawable.option_bg_true, null)
            optionContainer.startAnimation(scaleInOutAnimation)
            correctCount++
        } else {
            optionContainer!!.background =
                ResourcesCompat.getDrawable(resources, R.drawable.option_bg_false, null)
            val shakeAnimation = AnimationUtils.loadAnimation(activity, R.anim.shake_anim)
            optionContainer.startAnimation(shakeAnimation)
            val fadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
            answerOptionContainer?.setBackgroundResource(R.drawable.option_bg_true)
            answerOptionContainer?.startAnimation(fadeInAnimation)
            for (txtView in optionTextViewList) {
                if (txtView.text.toString() == answer!!.name) {
                    txtView.setTextColor(Color.WHITE)
                }
            }
        }
        for (opt in optionContainerList) {
            opt.isEnabled = false
        }
        optionTextView.setTextColor(Color.WHITE)
        enableNextButton()
    }

    private fun resetOptions() {
        val scaleAnimation = AnimationUtils.loadAnimation(activity, R.anim.scale_in_from_zero)
        for (optionContainer in optionContainerList) {
            optionContainer.background =
                ResourcesCompat.getDrawable(resources, R.drawable.option_bg, null)
            optionContainer.startAnimation(scaleAnimation)
            optionContainer.isEnabled = true
        }
        for (optionTextView in optionTextViewList) {
            optionTextView.setTextColor(resources.getColor(R.color.colorTextPrimary))
        }
        answerOptionContainer?.setBackgroundResource(R.drawable.option_bg)
        disableNextButton()
    }
}