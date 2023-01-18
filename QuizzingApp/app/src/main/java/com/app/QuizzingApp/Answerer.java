package com.app.QuizzingApp;


/**
 * A subclass of User which encodes the information for an Answerer (person answering questions
 * given by Questioner)
 */
public class Answerer extends User {
    // instance var
    private String questionerID; // active user (isActive) would find question set via questionerID

    // constructors
    /**
     * Default constructor for Answerer object
     */
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
        // instantiate super class (this class inherits from User)
        super(firstName, lastName, UID, email, password, false);
        this.questionerID = null;   // will be instantiated when Answerer is synced
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
