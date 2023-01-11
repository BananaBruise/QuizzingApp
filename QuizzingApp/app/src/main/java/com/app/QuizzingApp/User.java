package com.app.QuizzingApp;

import java.util.ArrayList;
import java.util.UUID;

public class User {
    // instance vars
    private String UID;
    private String fName;
    private String lName;
    private boolean isActive;
    private String email;
    private String password;
    private boolean isQuestioner;

    // constructor
    public User() {
    }

    public User(String firstName, String lastName, String UID, String email, String password, boolean isQuestioner) {
        this.UID = UID;
        this.fName = firstName;
        this.lName = lastName;
        this.isActive = false;
        this.isQuestioner = isQuestioner;
        this.email = email;
        this.password = password;
    }

    // getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isQuestioner() {
        return isQuestioner;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public boolean getisActive() {
        return isActive;
    }

    public String getUID() {
        return UID;
    }

    public String getName() {
        return this.fName + " " + this.lName;
    }

    // setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setQuestioner(boolean questioner) {
        isQuestioner = questioner;
    }

    protected void setfName(String fName) {
        this.fName = fName.trim();
    }

    protected void setlName(String lName) {
        this.lName = lName.trim();
    }

    protected void setisActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
