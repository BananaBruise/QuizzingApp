package com.app.QuizzingApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignInActivity extends AppCompatActivity {

    private EditText emailET, passwordET;

    public static FirebaseHelper firebaseHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseHelper = new FirebaseHelper();

        // if user context exists, check for sync right away
        if (firebaseHelper.getmAuth().getCurrentUser()!=null){
            String uid = firebaseHelper.getmAuth().getCurrentUser().getUid();
            User u = firebaseHelper.getUser(uid);
            // if user is not active, take them to sync screen based on role
            if (u.getisActive()==false){
                takeToSync(u.isQuestioner());
            }
            // otherwise is active, go to homescreen
            else{
                // TODO
            }

        }

        emailET = findViewById(R.id.signInEmailET);
        passwordET = findViewById(R.id.signInPasswordET);
    }

    public void takeToSignUp(View v) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    public void takeToSync(boolean Questioner){
        if (Questioner){
            // TODO
            // intent = new Intent(getApplicationContext(), QuestionerSyncActivity.class);
        }
        else
            startActivity(new Intent(getApplicationContext(), AnswererSyncActivity.class));

    }

    public void signIn(View v) {

        // Get user data
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();


        // verify all user data is entered
        if (email.length() == 0 || password.length() == 0) {
            Toast.makeText(getApplicationContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
        }
        else {
            // code to sign in user
            firebaseHelper.getmAuth().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Sign in successful", Toast.LENGTH_SHORT).show();
                                // this is another way to create the intent from inside the OnCompleteListener
                                if (firebaseHelper.getmAuth().getCurrentUser()!=null){
                                    String uid = firebaseHelper.getmAuth().getCurrentUser().getUid();
                                    User u = firebaseHelper.getUser(uid);
                                        // if user is not active, take them to sync screen based on role
                                        if (u.getisActive()==false){
                                            takeToSync(u.isQuestioner());
                                        }
                                        // otherwise is active, go to homescreen
                                        else{
                                            // TODO
                                        }

                                }
                            }
                            else {
                                //sign in failed
                                Log.d("TAG", email + " failed to log in" + task.getException());
                                Toast.makeText(getApplicationContext(), "Wrong email or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }



}
