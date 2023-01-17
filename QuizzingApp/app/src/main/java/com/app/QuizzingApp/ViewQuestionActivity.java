package com.app.QuizzingApp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Allows a user to view a question they have clicked on (from a list view)
 */
public class ViewQuestionActivity extends AppCompatActivity {

    TextView questionNameTV, answersTV;
    RatingBar difficultyRB;

    Question clickedQuestion;
    String answers;

    /**
     * Instantiate UI references and populate them with the data given by the intent sent from
     * previous activity
     * @param savedInstanceState
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        Intent intent = getIntent();

        clickedQuestion = (Question) intent.getParcelableExtra("ITEM");

        questionNameTV = findViewById(R.id.questionNameTV);
        difficultyRB = findViewById(R.id.difficultyRB);
        answersTV = findViewById(R.id.answersTV);

        questionNameTV.setText(clickedQuestion.getName());
        difficultyRB.setRating(clickedQuestion.getDiff());
        answersTV.setText(clickedQuestion.printAnswers());

    }
}