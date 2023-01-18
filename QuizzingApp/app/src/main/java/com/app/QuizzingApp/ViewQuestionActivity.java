package com.app.QuizzingApp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Allows a user to view a question they have clicked on (from a list view)
 */
public class ViewQuestionActivity extends AppCompatActivity {
    // UI elements
    TextView questionNameTV, answersTV;
    RatingBar difficultyRB;

    // question the user clicked on from the previous list view
    Question clickedQuestion;

    /**
     * Instantiate UI references and populate them with the data given by the intent sent from
     * previous activity
     * @param savedInstanceState may be used to restore activity to a previous state
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        // receive data from intent
        Intent intent = getIntent();
        clickedQuestion = (Question) intent.getParcelableExtra("ITEM");

        // set UI references
        questionNameTV = findViewById(R.id.questionNameTV);
        difficultyRB = findViewById(R.id.difficultyRB);
        answersTV = findViewById(R.id.answersTV);

        // set UI elements with data from question
        questionNameTV.setText(clickedQuestion.getName());
        difficultyRB.setRating(clickedQuestion.getDiff());
        answersTV.setText(clickedQuestion.printAnswers());

    }
}