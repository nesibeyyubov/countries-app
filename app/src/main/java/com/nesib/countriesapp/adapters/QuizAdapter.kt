package com.nesib.countriesapp.adapters

import android.content.Context
import com.nesib.countriesapp.models.Quiz
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.nesib.countriesapp.R
import android.widget.TextView
import com.nesib.countriesapp.utils.Constants
import java.text.MessageFormat

class QuizAdapter(private val quizList: List<Quiz>, private val context: Context) :
    RecyclerView.Adapter<QuizAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.quiz_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quiz = quizList[position]
        holder.quizTitle.text = quiz.title
        holder.quizSubTitle.text = quiz.subTitle
        holder.bestScore.text = MessageFormat.format("{0}", quiz.bestScore)
        when (quiz.quizType) {
            Constants.QUIZ_TYPE_FLAGS -> {
                holder.overlay.setBackgroundResource(R.color.colorOrangeDark)
            }
            Constants.QUIZ_TYPE_REGION -> {
                holder.overlay.setBackgroundResource(R.color.colorBlue)
            }
            else -> {
                holder.overlay.setBackgroundResource(R.color.colorPrimary)
            }
        }
    }

    override fun getItemCount(): Int {
        return quizList.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var quizTitle: TextView = itemView.findViewById(R.id.quizTitle)
        var quizSubTitle: TextView = itemView.findViewById(R.id.quizSubTitle)
        var questionCount: TextView = itemView.findViewById(R.id.questionCount)
        var bestScore: TextView = itemView.findViewById(R.id.bestScore)
        private var playButton: Button = itemView.findViewById(R.id.playButton)
        var overlay: View = itemView.findViewById(R.id.overlay)

        init {
            playButton.setOnClickListener {
                listener!!.onClick(
                    adapterPosition
                )
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(position: Int)
    }
}