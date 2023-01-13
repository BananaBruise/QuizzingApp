package com.app.QuizzingApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QuestionerDashboardActivity extends AppCompatActivity {

    FirebaseHelper firebaseHelper = new FirebaseHelper();
    EditText searchET;
    ArrayList<Question> questionsList = new ArrayList<>();

    @Override
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questioner_dashboard);

        searchET = findViewById(R.id.searchET);

        String uid = firebaseHelper.getmAuth().getCurrentUser().getUid();

        firebaseHelper.readQuestions(uid, new FirebaseHelper.FirestoreQuestionCallback() {
            @Override
            public void onCallbackQuestions(ArrayList<Question> questionList) {
                questionsList = questionList;
                displayQuestions(questionsList);
            }
        });
    }

    public void displayQuestions(ArrayList<Question> questionsList) {
        // check if there are no questions
        if (questionsList.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No questions yet! Add some.", Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<Question> listAdapter = new ArrayAdapter<Question>(
                getApplicationContext(), android.R.layout.simple_list_item_1, questionsList);


        ListView listView = (ListView) findViewById(R.id.allQuestionsLV);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewQuestionActivity.class);

                intent.putExtra("ITEM", questionsList.get(i));
                startActivity(intent);
            }
        });
    }

    public void takeToQuestionMaker(View v) {
        startActivity(new Intent(getApplicationContext(), QuestionMakerActivity.class));
    }

    public void signOut(View v) {
        new Navigation().signOut(QuestionerDashboardActivity.this);
    }

    public void searchHelper(View v) {
        String key = searchET.getText().toString();

        if (!key.isEmpty()) {
            search(key);
        } else {
            displayQuestions(questionsList);
        }
    }

    public void search(String questionName) {

        ArrayList<Question> searched = new ArrayList<Question>();

        for (int i = 0; i < questionsList.size(); i++) {
            if (questionsList.get(i).getName().equals(questionName)) {
                searched.add(questionsList.get(i));
            }
        }

        displayQuestions(searched);
    }
}

