package com.app.QuizzingApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.util.ArrayList;

public class QuestionerDashboardActivity extends AppCompatActivity {

    public static FirebaseHelper firebaseHelper = new FirebaseHelper();


    EditText searchET;

    ArrayList<Question> questionsList = new ArrayList<Question>();
    @Override
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questioner_dashboard);

        questionsList.clear();

        searchET = findViewById(R.id.searchET);

        String uid = firebaseHelper.getmAuth().getCurrentUser().getUid();

        firebaseHelper.getmdb().collection("Users").document(uid).collection("Questions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(DocumentSnapshot doc: task.getResult()) {
                                Log.i("TAG", doc.getData().toString());
                                questionsList.add(doc.toObject(Question.class));
                            }
                            Log.i("TAG", "success grabbing questions");
                            Log.i("TAG", questionsList.toString());

                            ArrayAdapter<Question> listAdapter = new ArrayAdapter<Question>(
                                    getApplicationContext(), android.R.layout.simple_list_item_1, questionsList);


                            ListView listView = (ListView) findViewById(R.id.listview);
                            listView.setAdapter(listAdapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(getApplicationContext(), ViewQuestionActivity.class);


                                    intent.putExtra("ITEM_TO_EDIT", questionsList.get(i));
                                    Log.i("ANSWERS", questionsList.get(i).printAnswers());
                                    intent.putExtra("ANSWERS", questionsList.get(i).printAnswers());
                                    startActivity(intent);

                                }
                            });
                        }
                    }
                });



    }

    public void takeToQuestionMaker(View v) {
        startActivity(new Intent(getApplicationContext(), QuestionMakerActivity.class));
    }

    public void signOut(View v) {
        firebaseHelper.getmAuth().signOut();

        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(i);
    }

    public void searchHelper(View v) {
        String key = searchET.getText().toString();

        if (!key.isEmpty()) {
            search(key);
        } else {
            ArrayAdapter<Question> listAdapter = new ArrayAdapter<Question>(
                    getApplicationContext(), android.R.layout.simple_list_item_1, questionsList);


            ListView listView = (ListView) findViewById(R.id.listview);
            listView.setAdapter(listAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), ViewQuestionActivity.class);


                    intent.putExtra("ITEM_TO_EDIT", questionsList.get(i));
                    Log.i("ANSWERS", questionsList.get(i).printAnswers());
                    intent.putExtra("ANSWERS", questionsList.get(i).printAnswers());
                    startActivity(intent);

                }
            });
        }
    }

    public void search(String questionName) {

        ArrayList<Question> searched = new ArrayList<Question>();

        for (int i = 0; i < questionsList.size(); i++) {
            if (questionsList.get(i).getName().equals(questionName)) {
                searched.add(questionsList.get(i));
            }
        }

        ArrayAdapter<Question> listAdapter = new ArrayAdapter<Question>(
                getApplicationContext(), android.R.layout.simple_list_item_1, searched);


        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewQuestionActivity.class);


                intent.putExtra("ITEM_TO_EDIT", searched.get(i));
                Log.i("ANSWERS", searched.get(i).printAnswers());
                intent.putExtra("ANSWERS", searched.get(i).printAnswers());
                startActivity(intent);

            }
        });
    }
}

