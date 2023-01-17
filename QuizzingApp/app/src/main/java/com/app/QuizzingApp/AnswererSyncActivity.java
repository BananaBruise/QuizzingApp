package com.app.QuizzingApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity that manages the syncing of an Answerer to a Questioner
 */
public class AnswererSyncActivity extends AppCompatActivity {

    // global variables
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    EditText codeET;

    /**
     * Instantiate references to views
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answerer_sync);

        codeET = findViewById(R.id.syncCodeET);
    }

    /**
     * Syncs an Answerer to a Questioner
     * @param v the view corresponding to the current screen
     */
    public void syncAnswerer(View v) {
        String otherUid = codeET.getText().toString();

        // callback used to sync two users
        firebaseHelper.syncUsers(otherUid, firebaseHelper.getmAuth().getCurrentUser().getUid(), new FirebaseHelper.FirestoreUserCallback() {
            @Override
            public void onCallbackUserSyncNamePair(String otherUserFirstName, String myUserFirstName) {
                Toast.makeText(getApplicationContext(), "You are synced to " + otherUserFirstName, Toast.LENGTH_SHORT).show();
                new Navigation().displayAlertDialog(AnswererSyncActivity.this, myUserFirstName, otherUserFirstName);
            }

            @Override
            public void onCallbackUserSyncFail() {
                Toast.makeText(getApplicationContext(), "You entered an invalid code", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Signs out a user
     * @param v the view corresponding to the current screen
     */
    public void signOut(View v) {
        firebaseHelper.getmAuth().signOut();

        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(i);
    }
}