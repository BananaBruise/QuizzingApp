package com.app.QuizzingApp;

import java.util.ArrayList;
import java.util.UUID;

// comment
public class User {

    private final String UID;
    private String fName;
    private String lName;
    private boolean isActive;
    private ArrayList<String> questions;

    public User(String firstName, String lastName, ArrayList<String> questions) {
        this.UID = UUID.randomUUID().toString();
        this.fName = firstName;
        this.lName = lastName;
        this.isActive = false;
        this.questions = questions;
    }

    public String getfName() {
        return fName;
    }

    protected void setfName(String fName) {
        this.fName = fName.trim();
    }

    public String getlName() {
        return lName;
    }

    protected void setlName(String lName) {
        this.lName = lName.trim();
    }

    public boolean getisActive() {
        return isActive;
    }

    protected void setisActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }

    public String getUID() {
        return UID;
    }

    public String getName() {
        return this.fName + " " + this.lName;
    }
}
