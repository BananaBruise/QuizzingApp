package com.app.QuizzingApp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PostQuizActivity extends AppCompatActivity {

    public static FirebaseHelper firebaseHelper = new FirebaseHelper();

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_quiz);

        Bundle extras = getIntent().getBundleExtra("INCORRECT_QUESTIONS");
        ArrayList<Question> wrongQuestions = extras.getParcelableArrayList("WRONGS");
        Log.i("answers", Integer.toString(wrongQuestions.get(0).getAnswers().size()));

        ArrayAdapter<Question> listAdapter = new ArrayAdapter<Question>(
                getApplicationContext(), android.R.layout.simple_list_item_1, wrongQuestions);


        ListView listView = (ListView) findViewById(R.id.wrongQuestionsListView);
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

    public void signOut(View v) {
        firebaseHelper.getmAuth().signOut();

        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(i);
    }

    public void retry(View v){
        Intent i = new Intent(getApplicationContext(), AnswererDashboardActivity.class);
        startActivity(i);
    }

}