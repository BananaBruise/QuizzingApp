package com.app.QuizzingApp;

import java.util.ArrayList;
import java.util.List;

/**
 * A subclass of User. Creates the Questions answered by the Answerer.
 */
public class Questioner extends User {
    // instance var
    private List<Question> questionList;

    // constructor
    /**
     * Default constructor for Questioner
     */
    public Questioner() {
    }

    /**
     * Creates a Questioner object with the given parameters
     * @param firstName first name of Questioner
     * @param lastName last name of Questioner
     * @param UID unique UID of Questioner
     * @param email email of Questioner
     * @param password password of Questioner
     */
    public Questioner(String firstName, String lastName, String UID, String email, String password) {
        super(firstName, lastName, UID, email, password, true);
        this.questionList = new ArrayList<Question>();
    }

    // getter
    /**
     * Getter method for questions posted by this Questioner
     * @return
     */
    public List<Question> getQuestionList() {
        return this.questionList;
    }
}
