package com.app.QuizzingApp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ViewQuestionActivity extends AppCompatActivity {

    TextView questionNameTV, difficultyTV, answersTV;

    Question clickedQuestion;
    String answers;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        Intent intent = getIntent();

        clickedQuestion = (Question) intent.getParcelableExtra("ITEM");

        questionNameTV = findViewById(R.id.questionNameTV);
        difficultyTV = findViewById(R.id.difficultyTV);
        answersTV = findViewById(R.id.answersTV);

        questionNameTV.setText(clickedQuestion.getName());
        difficultyTV.setText("difficulty: " + clickedQuestion.getDiff());
        answersTV.setText(clickedQuestion.printAnswers());

    }
}