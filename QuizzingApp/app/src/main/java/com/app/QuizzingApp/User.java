package com.app.QuizzingApp;

import java.util.ArrayList;

// comment
public class User {

    private String name;
    private int age;
    private ArrayList<String> questions;

    public User() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }

    public User(String name, int age, ArrayList<String> questions) {
        this.name = name;
        this.age = age;
        this.questions = questions;

    }
}
