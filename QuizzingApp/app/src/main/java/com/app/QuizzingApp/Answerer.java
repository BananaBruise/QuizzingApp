package com.app.QuizzingApp;

public class Answerer extends User {
    private String questionID; // active user (isActive) would find question set via QuestionID

    public Answerer(String firstName, String lastName, String UID, String email, String password) {
        super(firstName, lastName, UID, email, password, false);
        this.questionID = null;
    }

    public Answerer() {
    }

    public String getQuestionID() {
        return this.questionID;
    }
}
