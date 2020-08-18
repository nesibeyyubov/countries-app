package com.nesib.countriesapp.models;

public class Quiz {
    private String title;
    private String subTitle;
    private String duration;
    private String bestScore;
    private int questionCount;
    private String quizType;

    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }

    public Quiz(String title, String subTitle, String duration, String bestScore, int questionCount, String quizType) {
        this.title = title;
        this.subTitle = subTitle;
        this.duration = duration;
        this.bestScore = bestScore;
        this.questionCount = questionCount;
        this.quizType = quizType;
    }

    public Quiz() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBestScore() {
        return bestScore;
    }

    public void setBestScore(String bestScore) {
        this.bestScore = bestScore;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }


}
