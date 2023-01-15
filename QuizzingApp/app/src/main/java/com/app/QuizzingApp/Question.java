package com.app.QuizzingApp;

import android.os.Parcel;
import android.os.Parcelable;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Question implements Parcelable, Comparable<Question> {
    // instance vars
    private final int MAX_CHOICES = 4;
    private String name;
    private int diff;
    private List<Answer> answers;
    private boolean isCorrectlyAnsweredLastTime;
    private int questionID;
    private int millisElapsedToAnswer;

    // constructor
    public Question() {
    }

    public Question(String name, int diff, int questionID) {
        this.name = name;
        this.diff = diff;
        this.answers = new ArrayList<Answer>(MAX_CHOICES);
        this.isCorrectlyAnsweredLastTime = false;
        this.questionID = questionID;
    }

    public Question(Parcel parcel) {
        name = parcel.readString();
        diff = parcel.readInt();
        answers = parcel.createTypedArrayList(Answer.CREATOR);
    }

    // getter
    public String getName() {
        return name;
    }

    public int getMillisElapsedToAnswer() {
        return millisElapsedToAnswer;
    }

    public int getDiff() {
        return diff;
    }

    public int getQuestionID() {
        return questionID;
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }

    public boolean isCorrectlyAnsweredLastTime() {
        return isCorrectlyAnsweredLastTime;
    }

    public String printAnswers() {
        String output = "";

        for (int i = 0; i < MAX_CHOICES; i++) {
            output += answers.get(i).toString() + "\n";
        }

        return output.trim();
    }

    public String toString() {
        return this.name + ", difficulty: " + this.diff;
    }

    // setters
    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addAnswers(ArrayList<Answer> answers) {
        // list size must be under MAX_CHOICES
        if (answers.size() > MAX_CHOICES)
            throw new IllegalStateException("exceeds max number of choices of " + MAX_CHOICES);

        // add list
        this.answers = answers;
    }

    // Parcelable
    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {

        @Override
        public Question createFromParcel(Parcel parcel) {
            return new Question(parcel);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(name);
        dest.writeInt(diff);
        dest.writeTypedList(answers);
    }

    // Comparable
    @Override
    public int compareTo(Question question) {
        // 1. if the missed it last time (false)
        // 2. time spent answering
        // 3. difficulty

        // scenario 1: compare 1
        if (this.isCorrectlyAnsweredLastTime != question.isCorrectlyAnsweredLastTime) {
            if (this.isCorrectlyAnsweredLastTime && !question.isCorrectlyAnsweredLastTime) {
                return 1;
            } else if (question.isCorrectlyAnsweredLastTime && !this.isCorrectlyAnsweredLastTime) {
                return -1;
            }
        }

        // scenario 2: compare 2
        if (Math.abs(this.millisElapsedToAnswer - question.millisElapsedToAnswer) > 10) { //TODO 10000 set back
            return ((Integer) this.millisElapsedToAnswer).compareTo(question.millisElapsedToAnswer);
        }

        // scenario 3: compare 3
        if (this.diff != question.diff) {
            return ((Integer) this.diff).compareTo(question.diff);
        }

        return 0;
    }
}
