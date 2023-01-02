package com.app.QuizzingApp;

import java.util.ArrayList;
import java.util.List;

// comment
public class Question {
    private final int MAX_CHOICES = 4;
    private String name;
    private int diff;
    private List<Answer> answers;

    public Question(String name, int diff) {
        this.name = name;
        this.diff = diff;
        this.answers = new ArrayList<Answer>(MAX_CHOICES);
    }

    public Question() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDiff() {
        return diff;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }

    // TODO: implement these stubbed methods
    public void addAnswer(){
        // Answer list should not exceed MAX_CHOICES
        if (answers.size()>MAX_CHOICES)
            throw new IllegalStateException("exceeds max number of choices of " + MAX_CHOICES);

        return;
    }

    public void removeAnswer(){
        return;
    }

    public void clearAllAnswers(){
        return;
    }

    public String printAnswers(){
        return "answers";
    }

    public String printQuestion(){
        return "question";
    }

    public Boolean isQuestionCorrectlyAnswered(){
        return false;
    }
}
