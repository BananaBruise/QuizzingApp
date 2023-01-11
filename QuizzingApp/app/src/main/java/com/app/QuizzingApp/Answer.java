package com.app.QuizzingApp;

import android.os.Parcel;
import android.os.Parcelable;

public class Answer implements Parcelable {
    // instance vars
    private String prompt;
    private boolean isCorrect;

    // constructor
    public Answer() {
        this("Example prompt; please change me.", false);
    }

    public Answer(String prompt, boolean isCorrect) {
        this.prompt = prompt;
        this.isCorrect = isCorrect;
    }

    public Answer(Parcel parcel) {
        prompt = parcel.readString();
        isCorrect = parcel.readBoolean();
    }

    // getters
    public boolean getCorrect() {
        return isCorrect;
    }

    public String getPrompt() {
        return prompt;
    }

    public String toString() {
        return this.prompt + ", " + (isCorrect ? "correct!" : "incorrect!");
    }

    // setters
    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    // Parcelable
    public static final Parcelable.Creator<Answer> CREATOR = new Parcelable.Creator<Answer>() {

        @Override
        public Answer createFromParcel(Parcel parcel) {
            return new Answer(parcel);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(prompt);
        parcel.writeBoolean(isCorrect);
    }
}
