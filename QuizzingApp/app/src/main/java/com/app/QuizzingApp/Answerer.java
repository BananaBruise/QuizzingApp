package com.app.QuizzingApp;

import android.os.Parcel;
import android.os.Parcelable;

public class Answerer extends User {
    // instance var
    private String questionerID; // active user (isActive) would find question set via QuestionID

    // constructor
    public Answerer() {
    }

    public Answerer(String firstName, String lastName, String UID, String email, String password) {
        super(firstName, lastName, UID, email, password, false);
        this.questionerID = null;
    }

    // getter
    public String getQuestionerID() {
        return this.questionerID;
    }
}
