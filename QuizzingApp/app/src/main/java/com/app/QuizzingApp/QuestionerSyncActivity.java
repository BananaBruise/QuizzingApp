package com.app.QuizzingApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Manages our QuestionerSyncActivity screen
 */
public class QuestionerSyncActivity extends AppCompatActivity {

    TextView codeTV;
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    String userUid;

    /**
     * Instantiates references to UI elements as well as the uid corresponding to the current user
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questioner_sync);

        codeTV = findViewById(R.id.questionerSyncCodeTV);

        userUid = firebaseHelper.getmAuth().getCurrentUser().getUid();

        codeTV.setText("CODE: " + userUid);
    }

    /**
     * Syncs a Questioner with an Answerer (uses methods defined in FirebaseHelper class)
     * @param v view object corresponding to current screen
     */
    public void syncQuestioner(View v){
        // check if my isActive and other isActive is true

        firebaseHelper.readUser(userUid, new FirebaseHelper.FirestoreUserCallback() {
            @Override
            public void onCallbackUser(User u) {
                if (!u.getisActive()) {
                    Toast.makeText(getApplicationContext(), "You were not connected successfully to the student", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You were successfully synced to the student!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), QuestionerDashboardActivity.class));
                }
            }
        });
    }

    /**
     * Signs out the current user
     * @param v view object corresponding to the current screen
     */
    public void signOut(View v) {
        new Navigation().signOut(QuestionerSyncActivity.this);
    }
}