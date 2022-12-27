package com.app.QuizzingApp;

// comment
public class Question {
    private String name;
    private String diff;

    public Question(String name, String diff) {
        this.name = name;
        this.diff = diff;
    }

    public Question() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }
}
