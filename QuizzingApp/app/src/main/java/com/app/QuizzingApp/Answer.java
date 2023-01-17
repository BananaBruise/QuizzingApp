package com.app.QuizzingApp;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class contains the information for the creation of an Answer object, which will hold
 * a single answer (with its prompt and whether it is correct or not) to a multiple choice
 * Question.
 */
public class Answer implements Parcelable {
    // instance vars
    private String prompt;
    private boolean isCorrect;

    // constructor

    /**
     * Default constructor for Answer object
     */
    public Answer() {
        this("Example prompt; please change me.", false);
    }

    /**
     * Creates and instantiates an Answer object defined by its prompt and whether it is correct or
     * not
     * @param prompt the answer choice (written as a String)
     * @param isCorrect whether this answer is correct or not
     */
    public Answer(String prompt, boolean isCorrect) {
        this.prompt = prompt;
        this.isCorrect = isCorrect;
    }


    // getters

    /**
     * Getter for correctness status
     * @return true if this Answer is correct and false if it isn't
     */
    public boolean getCorrect() {
        return isCorrect;
    }

    /**
     * Getter for Answer prompt
     * @return the content of the Answer (as a String)
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * Returns a String representation of an Answer object with its prompt and correctness status
     * @return a String representation of an Answer object
     */
    public String toString() {
        return this.prompt + ", " + (isCorrect ? "correct!" : "INCORRECT!");
    }

    // setters
    /**
     * Setter for isCorrect field
     * @param correct the boolean we are updating isCorrect to
     */
    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    /**
     * Setter for prompt field
     * @param prompt the new prompt for this Answer
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    // Parcelable
    /**
     * Interface that must be implemented and provided as a public CREATOR field that generates
     * instances of this Parcelable class from a Parcel.
     */
    public static final Parcelable.Creator<Answer> CREATOR = new Parcelable.Creator<Answer>() {

        /**
         * Creates a new instance of the Parcelable (Answer) class, instantiating it from the
         * given Parcel
         * @param parcel the Parcel whose data had been previously written by Parcelable.writeToParcel()
         * @return a new instance of the Parcelable (Answer) class, instantiating it from the
         *         given Parcel
         */
        @Override
        public Answer createFromParcel(Parcel parcel) {
            return new Answer(parcel);
        }

        /**
         * Returns an array of the Parcelable class with every entry intitlaized to null
         * @param size size of the array
         * @return an array of the Parcelable class with every entry intitlaized to null
         */
        @Override
        public Answer[] newArray(int size) {
            return new Answer[0];
        }
    };

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled
     * representation
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flattens this object into a parcel
     * @param parcel the Parcel we are flattening this object into (unpack)
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(prompt);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            parcel.writeBoolean(isCorrect);
        }
    }

    /**
     * Used to unpack a Parcelable Answer object
     * @param parcel the parcel we are unpacking from
     */
    public Answer(Parcel parcel) {
        prompt = parcel.readString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isCorrect = parcel.readBoolean();
        }

    }
}
