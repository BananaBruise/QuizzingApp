package com.app.QuizzingApp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A subclass of User which encodes the information for an Answerer (person answering questions
 * given by Questioner)
 */
public class Answerer extends User {
    // instance var
    private String questionerID; // active user (isActive) would find question set via QuestionID

    /**
     * Default constructor for Answerer object
     */
    // constructor
    public Answerer() {
    }

    /**
     * Creates a new Answerer object with the given parameters
     * @param firstName first name of Answerer
     * @param lastName last name of Answerer
     * @param UID unique UID of Answerer
     * @param email email of Answerer
     * @param password password of Answerer
     */
    public Answerer(String firstName, String lastName, String UID, String email, String password) {
        super(firstName, lastName, UID, email, password, false);
        this.questionerID = null;
    }


    // getter
    /**
     * Getter method for questionerID field (the person who is questioning this Answerer)
     * @return questionerID field (described above)
     */
    public String getQuestionerID() {
        return this.questionerID;
    }
}
