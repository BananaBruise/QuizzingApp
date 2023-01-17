package com.app.QuizzingApp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Encodes the information for a Question object
 */
public class Question implements Parcelable, Comparable<Question> {
    // instance vars
    private final int MAX_CHOICES = 4;
    private String name;
    private int diff;
    private List<Answer> answers;
    private boolean correctlyAnsweredLastTime;
    private int questionID;
    private int millisElapsedToAnswer;

    /**
     * Default constructor for a Question object
     */
    // constructor
    public Question() {
    }

    /**
     * Creates a Question object with the given data
     * @param name name of this question
     * @param diff difficulty of this question
     * @param questionID question (document) ID of this question
     */
    public Question(String name, int diff, int questionID) {
        this.name = name;
        this.diff = diff;
        this.answers = new ArrayList<Answer>(MAX_CHOICES);
        this.correctlyAnsweredLastTime = false;
        this.questionID = questionID;

    }


    // getter

    /**
     * Getter for name of question
     * @return name of question
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for time elapsed to answer this question
     * @return time elapsed to answer this question
     */
    public int getMillisElapsedToAnswer() {
        return millisElapsedToAnswer;
    }

    /**
     * Getter for difficulty of this question
     * @return difficulty for this question
     */
    public int getDiff() {
        return diff;
    }

    /**
     * Getter for question (doc) ID of this Question
     * @return getter for questionID of this question
     */
    public int getQuestionID() {
        return questionID;
    }

    /**
     * Getter for the list of answers for this Question
     * @return the list of answers for this Question
     */
    public List<Answer> getAnswers() {
        return this.answers;
    }

    /**
     * Getter for whether or not this Question was correctly answered last time by the user
     * @return whether or not this Question was correctly answered last time by the user
     */
    public boolean isCorrectlyAnsweredLastTime() {
        return correctlyAnsweredLastTime;
    }

    /**
     * Returns a String representation of all the answers to this question
     * @return a String representation of all the answers to this question
     */
    public String printAnswers() {
        String output = "";

        for (int i = 0; i < MAX_CHOICES; i++) {
            output += answers.get(i).toString() + "\n";
        }

        return output.trim();
    }

    /**
     * Returns a String representation of this Question
     * @return a String representation of this Question
     */
    public String toString() {
        return this.name + ", difficulty: " + this.diff;
    }

    // setters

    /**
     * Setter for correctlyAnsweredLastTime
     * @param correctlyAnsweredLastTime new value for this boolean
     */
    public void setCorrectlyAnsweredLastTime(boolean correctlyAnsweredLastTime) {
        this.correctlyAnsweredLastTime = correctlyAnsweredLastTime;
    }

    /**
     * Setter for questionID
     * @param questionID new questionID
     */
    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    /**
     * Setter for difficulty
     * @param diff new difficulty
     */
    public void setDiff(int diff) {
        this.diff = diff;
    }

    /**
     * Setter for name
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the answer list of this Question
     * @param answers new answer list
     */
    public void addAnswers(ArrayList<Answer> answers) {
        // list size must be under MAX_CHOICES
        if (answers.size() > MAX_CHOICES)
            throw new IllegalStateException("exceeds max number of choices of " + MAX_CHOICES);

        // add list
        this.answers = answers;
    }

    // Parcelable
    /**
     * Interface that must be implemented and provided as a public CREATOR field that generates
     * instances of this Parcelable class from a Parcel.
     */
    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {

        /**
         * Creates a new instance of the Parcelable (Question) class, instantiating it from the
         * given Parcel
         * @param parcel the Parcel whose data had been previously written by Parcelable.writeToParcel()
         * @return a new instance of the Parcelable (Question) class, instantiating it from the
         *         given Parcel
         */
        @Override
        public Question createFromParcel(Parcel parcel) {
            return new Question(parcel);
        }

        /**
         * Returns an array of the Parcelable class with every entry intitlaized to null
         * @param size size of the array
         * @return an array of the Parcelable class with every entry intitlaized to null
         */
        @Override
        public Question[] newArray(int size) {
            return new Question[0];
        }
    };

    /**
     * Used to unpack a Parcelable Question object
     * @param parcel the parcel we are unpacking from
     */
    public Question(Parcel parcel) {
        name = parcel.readString();
        diff = parcel.readInt();
        answers = parcel.createTypedArrayList(Answer.CREATOR);
    }

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
     * @param dest the Parcel we are flattening this object into (unpack)
     * @param i
     */
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(name);
        dest.writeInt(diff);
        dest.writeTypedList(answers);
    }

    // Comparable

    /**
     * Used to sort our Question objects by priority. Order of ranking:
     * 1. if the user missed the question last time (higher priority)
     * 2. time the user spent answering the question (higher time = higher priority)
     * 3. difficulty of question (higher difficulty = higher priority)
     * @param question question we are comparing with "this" to see the higher priority element
     * @return a positive int if "this" was higher priority than question, a negative int if "this" was
     * lower priority than question, and 0 if they're the same priority
     */
    @Override
    public int compareTo(Question question) {
        // 1. if they missed it last time (false)
        // 2. time spent answering
        // 3. difficulty

        // scenario 1: compare 1
        if (this.correctlyAnsweredLastTime != question.correctlyAnsweredLastTime) {
            if (!this.correctlyAnsweredLastTime && question.correctlyAnsweredLastTime) {
                return 1;
            } else if (!question.correctlyAnsweredLastTime && this.correctlyAnsweredLastTime) {
                return -1;
            }
        }

        // scenario 2: compare 2
        if (Math.abs(this.millisElapsedToAnswer - question.millisElapsedToAnswer) > 10000) {
            return ((Integer) this.millisElapsedToAnswer).compareTo(question.millisElapsedToAnswer);
        }

        // scenario 3: compare 3
        if (this.diff != question.diff) {
            return ((Integer) this.diff).compareTo(question.diff);
        }

        return 0;
    }
}
