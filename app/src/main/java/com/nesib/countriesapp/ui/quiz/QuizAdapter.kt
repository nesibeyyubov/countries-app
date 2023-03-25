package com.nesib.countriesapp.ui.quiz

import android.view.LayoutInflater
import com.nesib.countriesapp.base.GenericListAdapter
import com.nesib.countriesapp.databinding.QuizItemBinding
import com.nesib.countriesapp.models.Quiz

class QuizAdapter(val onClickListener: (Quiz) -> Unit) : GenericListAdapter<QuizItemBinding, Quiz>(
    inflate = { context, parent, attachToParent ->
        QuizItemBinding.inflate(LayoutInflater.from(context), parent, false)
    },
    onBind = { quiz, index, quizItemBinding ->
        val context = quizItemBinding.root.context
        with(quizItemBinding) {
            overlay.setBackgroundResource(quiz.quizType.backgroundColorResource)
            quizTitle.text = context.getString(quiz.title)
            quizSubTitle.text = context.getString(quiz.subTitle)


            playButton.setOnClickListener { onClickListener(quiz) }
        }
    }
) {
}