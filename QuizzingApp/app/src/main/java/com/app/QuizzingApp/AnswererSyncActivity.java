package com.app.QuizzingApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AnswererSyncActivity extends AppCompatActivity {

    // global variables
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    EditText codeET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answerer_sync);

        codeET = findViewById(R.id.syncCodeET);
    }

    public void syncAnswerer(View v) {
        String otherUid = codeET.getText().toString();

        firebaseHelper.syncUsers(otherUid, firebaseHelper.getmAuth().getCurrentUser().getUid(), new FirebaseHelper.FirestoreUserCallback() {
            @Override
            public void onCallbackUserSyncSuccess(String otherUserFirstName) {
                Toast.makeText(getApplicationContext(), "You are synced to " + otherUserFirstName, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), AnswererDashboardActivity.class));
            }

            @Override
            public void onCallbackUserSyncFail() {
                Toast.makeText(getApplicationContext(), "You entered an invalid code", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void signOut(View v) {
        firebaseHelper.getmAuth().signOut();

        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(i);
    }
}