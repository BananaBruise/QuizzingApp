package com.app.QuizzingApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AnswererDashboardActivity extends AppCompatActivity {

    public static FirebaseHelper firebaseHelper = new FirebaseHelper();

    ArrayList<Question> questionsList = new ArrayList<Question>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        questionsList.clear();
        String uid = firebaseHelper.getmAuth().getCurrentUser().getUid();

        firebaseHelper.readGenericUser(uid, Answerer.class, new FirebaseHelper.FirestoreCallback() {
            @Override
            public void onCallbackUser(User u) {
                Answerer otherUser = (Answerer) u;
                String questionerID = otherUser.getQuestionerID();

                firebaseHelper.getmdb().collection("Users").document(questionerID).collection("Questions")
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


                                    ListView listView = (ListView) findViewById(R.id.answererlistview);
                                    listView.setAdapter(listAdapter);
                                }
                            }
                        });
            }
        });

    }

    public void signOut(View v) {
        firebaseHelper.getmAuth().signOut();

        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(i);
    }

    // populate answerlistview
    // TODO: Tinder-like swiping with Binding layout
    // references:
    // binding and cardstack tutorial: https://www.youtube.com/watch?v=3HvfQ_B7-RQ
    // binding documentation: https://developer.android.com/topic/libraries/view-binding
    // recycleView doc: https://developer.android.com/develop/ui/views/layout/recyclerview
    // tinder swiping module (CardStackView): https://github.com/yuyakaido/CardStackView
}