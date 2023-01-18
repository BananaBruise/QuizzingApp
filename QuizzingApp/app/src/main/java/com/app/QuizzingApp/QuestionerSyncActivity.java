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

    TextView codeTV;    // reference to TV displaying sync code
    FirebaseHelper firebaseHelper = new FirebaseHelper();   // reference to helper class
    String userUid; // uid of current user

    /**
     * Instantiates references to UI elements as well as the uid corresponding to the current user
     * @param savedInstanceState may be used to restore activity to a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questioner_sync);

        codeTV = findViewById(R.id.questionerSyncCodeTV);   // instantiate codeTV

        // get uid of current user
        userUid = firebaseHelper.getmAuth().getCurrentUser().getUid();

        // set codeTV appropriately
        codeTV.setText("CODE: " + FirebaseHelper.getShorterString(userUid));
    }

    /**
     * Syncs a Questioner with an Answerer (uses methods defined in FirebaseHelper class)
     * @param v view object corresponding to current screen
     */
    public void syncQuestioner(View v){
        // check if my isActive and other isActive is true
        firebaseHelper.readUser(userUid, new FirebaseHelper.FirestoreUserCallback() {
            @Override
            public void onCallbackReadUser(User u) {
                if (!u.getisActive()) {
                    // if i am not active, sync failed
                    Toast.makeText(getApplicationContext(), "You were not connected successfully to the student", Toast.LENGTH_SHORT).show();
                } else {
                    // if i am active, sync succeeded
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