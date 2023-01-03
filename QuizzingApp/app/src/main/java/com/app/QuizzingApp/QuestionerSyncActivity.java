package com.app.QuizzingApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionerSyncActivity extends AppCompatActivity {

    TextView codeTV;
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questioner_sync);

        codeTV = findViewById(R.id.CodeTV);

        String userUid = firebaseHelper.getmAuth().getCurrentUser().getUid();

        codeTV.setText("CODE: " + userUid);
    }

    public void syncQuestioner(View v){
        // check if my isActive and other isActive is true

        firebaseHelper.readUser(firebaseHelper.getmAuth().getCurrentUser().getUid(), new FirebaseHelper.FirestoreCallback() {
            @Override
            public void onCallbackUser(User u) {
                if (!u.getisActive()) {
                    Toast.makeText(getApplicationContext(), "You were not connected successfully to the student", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You were successfully to the student!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), QuestionerDashboardActivity.class));
                }


            }
        });



    }

    public void signOut(View v) {
        firebaseHelper.getmAuth().signOut();

        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(i);
    }
}