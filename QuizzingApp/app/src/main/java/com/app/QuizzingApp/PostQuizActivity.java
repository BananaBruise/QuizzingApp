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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Handles what needs to be done after an Answerer completes a quiz
 */
public class PostQuizActivity extends AppCompatActivity {

    TextView postQuizInfoTV;

    /**
     * Instantiates a list view with the Questions the Answerer got wrong from the previous quiz
     * @param savedInstanceState
     */
    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_quiz);

        postQuizInfoTV = findViewById(R.id.postQuizInfoTV);

        Bundle extras = getIntent().getBundleExtra("INCORRECT_QUESTIONS");
        ArrayList<Question> wrongQuestions = extras.getParcelableArrayList("WRONGS");

        // check if there were no questions missed
        if (wrongQuestions.isEmpty()) {
            postQuizInfoTV.setText("You got a perfect score!!");
            Toast.makeText(getApplicationContext(), "You got a perfect score!", Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<Question> listAdapter = new ArrayAdapter<Question>(
                getApplicationContext(), android.R.layout.simple_list_item_1, wrongQuestions);


        ListView listView = (ListView) findViewById(R.id.allQuestionsLV);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewQuestionActivity.class);


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