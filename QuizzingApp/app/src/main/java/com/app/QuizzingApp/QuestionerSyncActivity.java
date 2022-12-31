package com.app.QuizzingApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class QuestionerSyncActivity extends AppCompatActivity {

    FirebaseHelper firebaseHelper = new FirebaseHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questioner_sync);
    }

    public void syncQuestioner(View v){
        // update User object

        // if User.isActive is true --> go to next screen

        // else --> toast of failed sync
    }

    public void signOut(View v) {
        firebaseHelper.getmAuth().signOut();

        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(i);
    }
}