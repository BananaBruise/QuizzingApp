package com.app.QuizzingApp;

import android.os.Parcel;
import android.os.Parcelable;

public class Answer implements Parcelable {
    private String prompt;
    private Boolean isCorrect;

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

    public Answer(String prompt, Boolean isCorrect){
        this.prompt = prompt;
        this.isCorrect = isCorrect;
    }

    public Answer(Parcel parcel) {
        prompt = parcel.readString();
        isCorrect = parcel.readBoolean();
    }

    public Answer(){
        this("Example prompt; please change me.", false);
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String toString() {
        return this.prompt + ", " + (isCorrect ? "correct!" : "incorrect!");
    }

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
