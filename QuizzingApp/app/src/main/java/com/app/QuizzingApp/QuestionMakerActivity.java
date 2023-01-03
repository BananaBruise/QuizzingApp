package com.app.QuizzingApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

public class QuestionMakerActivity extends AppCompatActivity {
    // layout
    EditText qName;
    RatingBar difficulty;
    CheckBox correct1;
    EditText answerText1;
    CheckBox correct2;
    EditText answerText2;
    CheckBox correct3;
    EditText answerText3;
    CheckBox correct4;
    EditText answerText4;

    public static FirebaseHelper firebaseHelper = new FirebaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_maker);

        qName = findViewById(R.id.QuestionNameET);
        difficulty = findViewById(R.id.difficultyBar);
        correct1 = findViewById(R.id.qCorrectBox1);
        answerText1 = findViewById(R.id.answerTextET_1);
        correct2 = findViewById(R.id.qCorrectBox2);
        answerText2 = findViewById(R.id.answerTextET_2);
        correct3 = findViewById(R.id.qCorrectBox3);
        answerText3 = findViewById(R.id.answerTextET_3);
        correct4 = findViewById(R.id.qCorrectBox4);
        answerText4 = findViewById(R.id.answerTextET_4);
    }

    public void submitQuestion(View v) {
        // question attributes
        String name = qName.getText().toString();
        int diff = (int) difficulty.getRating();

        // answer attributes
        boolean c1 = correct1.isChecked();
        String t1 = answerText1.getText().toString();
        boolean c2 = correct2.isChecked();
        String t2 = answerText2.getText().toString();
        boolean c3 = correct3.isChecked();
        String t3 = answerText3.getText().toString();
        boolean c4 = correct4.isChecked();
        String t4 = answerText4.getText().toString();

        // check blank
        if (t1.isEmpty() || t2.isEmpty() || t3.isEmpty() || t4.isEmpty() || name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill in all fields!", Toast.LENGTH_SHORT).show();
        } else {
            // create our answers and question
            // question
            Question q = new Question(name, diff);
            // answers
            Answer a1 = new Answer(t1, c1);
            Answer a2 = new Answer(t2, c2);
            Answer a3 = new Answer(t3, c3);
            Answer a4 = new Answer(t4, c4);
            // add answers to question
            q.addAnswers(new ArrayList<Answer>(Arrays.asList(a1, a2, a3, a4)));

            // submit as a collection to current ID document
            FirebaseFirestore db = firebaseHelper.getmdb();
            String uid = firebaseHelper.getmAuth().getCurrentUser().getUid();
            int qid = (int) (Math.random() * 1000000);

            db.collection("Users").document(uid).collection("Questions").document(Integer.toString(qid))
                    .set(q)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.i("TAG", "question added");
                            Toast.makeText(getApplicationContext(), "Question added successfully!", Toast.LENGTH_SHORT).show();
                            // take back
                            startActivity(new Intent(getApplicationContext(), QuestionerDashboardActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "Error adding question", e);
                            Toast.makeText(getApplicationContext(), "Error adding question", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }



}