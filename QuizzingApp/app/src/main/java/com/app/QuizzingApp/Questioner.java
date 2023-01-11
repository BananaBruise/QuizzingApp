package com.app.QuizzingApp;

import java.util.ArrayList;
import java.util.List;

public class Questioner extends User {
    // instance var
    private List<Question> questionList;

    // constructor
    public Questioner() {
    }

    public Questioner(String firstName, String lastName, String UID, String email, String password) {
        super(firstName, lastName, UID, email, password, true);
        this.questionList = new ArrayList<Question>();
    }

    // getter
    public List<Question> getQuestionList() {
        return this.questionList;
    }
}
