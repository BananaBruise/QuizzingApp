package com.app.QuizzingApp;

import java.util.ArrayList;
import java.util.UUID;

// comment
public class User {

    private String UID;
    private String fName;
    private String lName;
    private boolean isActive;
    private String email;
    private String password;
    private boolean isQuestioner;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {}

    public void setUID(String UID) {
        this.UID = UID;
    }

    public boolean isQuestioner() {
        return isQuestioner;
    }

    public void setQuestioner(boolean questioner) {
        isQuestioner = questioner;
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


    public String getUID() {
        return UID;
    }

    public String getName() {
        return this.fName + " " + this.lName;
    }
}
