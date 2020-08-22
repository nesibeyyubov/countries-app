package com.nesib.countriesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nesib.countriesapp.Constants;
import com.nesib.countriesapp.R;
import com.nesib.countriesapp.models.Quiz;

import java.text.MessageFormat;
import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {
    private List<Quiz> quizList;
    private Context context;
    private OnItemClickListener listener;

    public QuizAdapter(List<Quiz> quizList, Context context) {
        this.quizList = quizList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.quizTitle.setText(quiz.getTitle());
        holder.quizSubTitle.setText(quiz.getSubTitle());
        holder.bestScore.setText(MessageFormat.format("{0}", quiz.getBestScore()));
        if(quiz.getQuizType().equals(Constants.QUIZ_TYPE_FLAGS)){
            holder.overlay.setBackgroundResource(R.color.colorOrangeDark);
        }
        else if(quiz.getQuizType().equals(Constants.QUIZ_TYPE_REGION)){
            holder.overlay.setBackgroundResource(R.color.colorBlue);
        }
        else{
            holder.overlay.setBackgroundResource(R.color.colorPrimary);
        }
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView quizTitle,quizSubTitle,questionCount,bestScore;
        public Button playButton;
        public View overlay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quizTitle = itemView.findViewById(R.id.quizTitle);
            quizSubTitle = itemView.findViewById(R.id.quizSubTitle);
            questionCount = itemView.findViewById(R.id.questionCount);
            bestScore = itemView.findViewById(R.id.bestScore);
            playButton = itemView.findViewById(R.id.playButton);
            overlay = itemView.findViewById(R.id.overlay);

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(getAdapterPosition());
                }
            });
        }
    }


    public interface OnItemClickListener{
        void onClick(int position);
    }
}
