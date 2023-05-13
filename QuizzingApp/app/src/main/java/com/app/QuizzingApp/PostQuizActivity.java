package com.app.QuizzingApp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Handles what needs to be done after an Answerer completes a quiz
 */
public class PostQuizActivity extends AppCompatActivity {

    TextView postQuizInfoTV;    // reference to main TV on screen
    ImageButton postQuizRetryInfoBT; // describes how we sort questions

    /**
     * Instantiates a list view with the Questions the Answerer got wrong from the previous quiz
     * @param savedInstanceState may be used to restore activity to a previous state
     */
    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_quiz);

        // initialize references
        postQuizInfoTV = findViewById(R.id.postQuizInfoTV);
        postQuizRetryInfoBT = findViewById(R.id.postQuizRetryInfoBT);

        // set up button
        postQuizRetryInfoBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Navigation().displaySortingDialog(PostQuizActivity.this);
            }
        });

        // retrieve the AL we sent to this page of wrong questions
        Bundle extras = getIntent().getBundleExtra("INCORRECT_QUESTIONS");
        ArrayList<Question> wrongQuestions = extras.getParcelableArrayList("WRONGS");

        // check if there were no questions missed
        if (wrongQuestions.isEmpty()) {
            postQuizInfoTV.setText("You got a perfect score!!");
            Toast.makeText(getApplicationContext(), "You got a perfect score!", Toast.LENGTH_LONG).show();
        }

        // display all wrong questions in a list view
        ArrayAdapter<Question> listAdapter = new QuestionListAdapter(
                getApplicationContext(), wrongQuestions);


        ListView listView = (ListView) findViewById(R.id.allQuestionsLV);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // user can click on individual questions they got wrong
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewQuestionActivity.class);

                // send over the question the user clicked to ViewQuestionActivity
                intent.putExtra("ITEM", wrongQuestions.get(i));
                startActivity(intent);

            }
        });

    }

    /**
     * Signs out the current user
     * @param v view corresponding to the current screen
     */
    public void signOut(View v) {
        new Navigation().signOut(PostQuizActivity.this);
    }

    /**
     * Allows a user to retake a quiz
     * @param v view corresponding to the current screen
     */
    public void retry(View v){
        Intent i = new Intent(getApplicationContext(), AnswererDashboardActivity.class);
        startActivity(i);
    }

}