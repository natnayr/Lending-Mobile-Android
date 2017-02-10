package com.crowdo.p2pmobile.model;

import io.realm.RealmObject;

/**
 * Created by cwdsg05 on 9/2/17.
 */

public class LearningCenter extends RealmObject{

    private String category;
    private String language;
    private String question;
    private String answer;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
