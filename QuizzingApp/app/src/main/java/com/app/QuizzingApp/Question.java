package com.app.QuizzingApp;

import android.os.Parcel;
import android.os.Parcelable;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

// comment
public class Question implements Parcelable, Comparable<Question> {
    private final int MAX_CHOICES = 4;
    private String name;
    private int diff;
    private List<Answer> answers;
    private boolean isCorrectlyAnsweredLastTime;
    private int questionID;

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

    public Question(Parcel parcel) {
        name = parcel.readString();
        diff = parcel.readInt();
        answers = parcel.createTypedArrayList(Answer.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(name);
        dest.writeInt(diff);
        dest.writeTypedList(answers);
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public Question(String name, int diff, int questionID) {
        this.name = name;
        this.diff = diff;
        this.answers = new ArrayList<Answer>(MAX_CHOICES);
        this.isCorrectlyAnsweredLastTime = false;
        this.questionID = questionID;
    }

    // constructor
    public Question() {}

    // getter
    public String getName() {
        return name;
    }


    public int getDiff() {
        return diff;
    }

    public List<Answer> getAnswers() {return this.answers;}

    public boolean isCorrectlyAnsweredLastTime() {
        return isCorrectlyAnsweredLastTime;
    }

    public String printAnswers(){
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
    public void setDiff(int diff) {
        this.diff = diff;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addAnswers(ArrayList<Answer> answers){
        // list size must be under MAX_CHOICES
        if (answers.size()>MAX_CHOICES)
            throw new IllegalStateException("exceeds max number of choices of " + MAX_CHOICES);

        // add list
        this.answers = answers;
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

    public String printQuestion(){
        return "question";
    }

    @Override
    public int compareTo(Question question) {
//        1. if the missed it last time (false)
//        2. higher difficulty

        return 0;
    }
}
