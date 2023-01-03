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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class SignInActivity extends AppCompatActivity {

    private EditText emailET, passwordET;

    public static FirebaseHelper firebaseHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // initialize helper class
        firebaseHelper = new FirebaseHelper();

        // grab view elements
        emailET = findViewById(R.id.signInEmailET);
        passwordET = findViewById(R.id.signInPasswordET);


        // checking for already signed in user
        if (firebaseHelper.getmAuth().getCurrentUser()!=null) {
            takeToPostSignIn(firebaseHelper.getmAuth().getCurrentUser().getUid());
        }



    }

    public void takeToSignUp(View v) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    public void takeToPostSignIn(String uid) {
        firebaseHelper.readUser(firebaseHelper.getmAuth().getCurrentUser().getUid(), new FirebaseHelper.FirestoreCallback() {
            @Override
            public void onCallbackUser(User u) {
                if (u.getisActive()==false && u.isQuestioner() == false) {
                    startActivity(new Intent(getApplicationContext(), AnswererSyncActivity.class));
                }
                else if (u.getisActive()==false && u.isQuestioner() == true) {
                    startActivity(new Intent(getApplicationContext(), QuestionerSyncActivity.class));
                } else if (u.getisActive() == true && u.isQuestioner()){
                    startActivity(new Intent(getApplicationContext(), QuestionerDashboardActivity.class));
                } else if (u.getisActive() == true && u.isQuestioner() == false) {
                    // TODO: ans dashboard
                }
            }
        });
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
                                takeToPostSignIn(firebaseHelper.getmAuth().getCurrentUser().getUid()); // direct authenticated user to next activity
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
