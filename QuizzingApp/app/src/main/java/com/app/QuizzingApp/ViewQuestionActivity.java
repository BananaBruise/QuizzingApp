package com.app.QuizzingApp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows a user to view a question they have clicked on (from a list view)
 */
public class ViewQuestionActivity extends AppCompatActivity {
    // UI elements
    TextView questionNameTV;

    EditText answer1ET, answer2ET, answer3ET, answer4ET;
    ImageView correctImg1, correctImg2, correctImg3, correctImg4;

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

        List<ImageView> correctImageViews = new ArrayList<>();
        List<EditText> answerEditTexts = new ArrayList<>();

       correctImg1 = findViewById(R.id.correctImg1);
       correctImg2 = findViewById(R.id.correctImg2);
       correctImg3 = findViewById(R.id.correctImg3);
       correctImg4 = findViewById(R.id.correctImg4);

        answer1ET = findViewById(R.id.answer1ET);
        answer2ET = findViewById(R.id.answer2ET);
        answer3ET = findViewById(R.id.answer3ET);
        answer4ET = findViewById(R.id.answer4ET);

        correctImageViews.add(correctImg1);
        correctImageViews.add(correctImg2);
        correctImageViews.add(correctImg3);
        correctImageViews.add(correctImg4);

        answerEditTexts.add(answer1ET);
        answerEditTexts.add(answer2ET);
        answerEditTexts.add(answer3ET);
        answerEditTexts.add(answer4ET);




        // set UI elements with data from question
        questionNameTV.setText(clickedQuestion.getName());
        difficultyRB.setRating(clickedQuestion.getDiff());

        // get answers to question
        List<Answer> answers = clickedQuestion.getAnswers();

        // set fields
        for (int i = 0; i < answers.size(); i++) {
            // if answer is correct, check the box
            if (answers.get(i).getCorrect()) {
                correctImageViews.get(i).setImageResource(R.drawable.ic_baseline_check_24);
            } else {
                correctImageViews.get(i).setImageResource(R.drawable.ic_baseline_close_24);
            }

            // populate edit text
            answerEditTexts.get(i).setText(answers.get(i).getPrompt());
        }



    }
}