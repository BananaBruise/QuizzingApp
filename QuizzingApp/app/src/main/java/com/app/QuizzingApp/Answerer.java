package com.app.QuizzingApp;

public class Answerer extends User {
    private String questionerID; // active user (isActive) would find question set via QuestionID

    public Answerer(String firstName, String lastName, String UID, String email, String password) {
        super(firstName, lastName, UID, email, password, false);
        this.questionerID = null;
    }

    public Answerer() {
    }

    public String getQuestionerID() {
        return this.questionerID;
    }
}
