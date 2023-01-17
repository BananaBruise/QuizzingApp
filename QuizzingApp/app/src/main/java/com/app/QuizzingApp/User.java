package com.app.QuizzingApp;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Main User object; contains information for a generic User
 */
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

    /**
     * Default constructor for User object
     */
    public User() {
    }

    /**
     * Creates a User object with the given parameters
     * @param firstName first name of User
     * @param lastName last name of User
     * @param UID unique UID of user
     * @param email email of user
     * @param password password of user
     * @param isQuestioner whether the User is a Questioner or not
     */
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

    /**
     * Getter for email
     * @return email of User
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for password
     * @return password of User
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter for isQuestioner
     * @return whether User is a Questioner or not
     */
    public boolean isQuestioner() {
        return isQuestioner;
    }

    /**
     * Getter for fName
     * @return first name of user
     */
    public String getfName() {
        return fName;
    }

    /**
     * Getter for lName
     * @return last name of user
     */
    public String getlName() {
        return lName;
    }

    /**
     * Getter for isActive status of user
     * @return whether user is active or not
     */
    public boolean getisActive() {
        return isActive;
    }

    /**
     * Getter for UID of user
     * @return unique UID of user
     */
    public String getUID() {
        return UID;
    }

    /**
     * Getter for full name of user
     * @return a String representation of the user's full name
     */
    public String getName() {
        return this.fName + " " + this.lName;
    }

    // setters

    /**
     * Setter for email
     * @param email new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Setter for password
     * @param password new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Setter for UID
     * @param UID new UID
     */
    public void setUID(String UID) {
        this.UID = UID;
    }

    /**
     * Setter for isQuestioner status
     * @param questioner new isQuestioner status
     */
    public void setQuestioner(boolean questioner) {
        isQuestioner = questioner;
    }

    /**
     * Setter for first name
     * @param fName new first name
     */
    protected void setfName(String fName) {
        this.fName = fName.trim();
    }

    /**
     * Setter for last name
     * @param lName new last name
     */
    protected void setlName(String lName) {
        this.lName = lName.trim();
    }

    /**
     * Setter for isActive status
     * @param isActive new isActive status
     */
    protected void setisActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
