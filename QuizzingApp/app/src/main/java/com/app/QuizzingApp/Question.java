package com.app.QuizzingApp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

// comment
public class Question implements Parcelable {
    private final int MAX_CHOICES = 4;
    private String name;
    private int diff;
    private List<Answer> answers;

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
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(name);
        dest.writeInt(diff);
    }



    public Question(String name, int diff) {
        this.name = name;
        this.diff = diff;
        this.answers = new ArrayList<Answer>(MAX_CHOICES);
    }

    // constructor
    public Question() {}

    // getter
    public String getName() {
        return name;
    }

    public Boolean isQuestionCorrectlyAnswered(){
        return false;
    }

    public int getDiff() {
        return diff;
    }

    public List<Answer> getAnswers() {return this.answers;}

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

    public String printAnswers(){
        String output = "";

        for (int i = 0; i < MAX_CHOICES; i++) {
            output += answers.get(i).toString() + "\n";
        }

        return output.trim();
    }

    public String printQuestion(){
        return "question";
    }

    public String toString() {
        return this.name + ", difficulty: " + this.diff;
    }
}
