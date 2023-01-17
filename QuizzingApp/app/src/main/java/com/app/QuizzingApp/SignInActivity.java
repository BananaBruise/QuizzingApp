package com.app.QuizzingApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

/**
 * First screen the user sees; will take them to the appropriate screen if they're already signed in/
 * if they're new/ if they're not active
 */
public class SignInActivity extends AppCompatActivity {

    private EditText emailET, passwordET;

    FirebaseHelper firebaseHelper;

    /**
     * Instantiates referneces to UI elements and also takes user to correct screen upon opening the
     * app
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // initialize helper class
        firebaseHelper = new FirebaseHelper();

        // grab view elements
        emailET = findViewById(R.id.signInEmailET);
        passwordET = findViewById(R.id.signInPasswordET);

        // checking for already signed in user
        if (firebaseHelper.getmAuth().getCurrentUser() != null) {
            takeToPostSignIn(firebaseHelper.getmAuth().getCurrentUser().getUid());
        }
    }

    /**
     * Takes the user to the SignUpActivity
     * @param v the view corresponding to the current scren
     */
    public void takeToSignUp(View v) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    /**
     * Depending on the user's isActive status and isQuestioner status, this method will take a user
     * to the correct activity
     * @param uid uid of current user
     */
    public void takeToPostSignIn(String uid) {

        firebaseHelper.readUser(uid, new FirebaseHelper.FirestoreUserCallback() {
            @Override
            public void onCallbackUser(User u) {
                if (u.getisActive() == false && u.isQuestioner() == false) {
                    startActivity(new Intent(getApplicationContext(), AnswererSyncActivity.class));
                } else if (u.getisActive() == false && u.isQuestioner() == true) {
                    startActivity(new Intent(getApplicationContext(), QuestionerSyncActivity.class));
                } else if (u.getisActive() == true && u.isQuestioner()) {
                    startActivity(new Intent(getApplicationContext(), QuestionerDashboardActivity.class));
                } else if (u.getisActive() == true && u.isQuestioner() == false) {
                    String questionerUID = ((Answerer) u).getQuestionerID();
                    firebaseHelper.readSyncedPair(questionerUID, u.getUID(), new FirebaseHelper.FirestoreUserCallback() {
                        @Override
                        public void onCallbackUserSyncNamePair(String otherUserFirstName, String myUserFirstName) {
                            new Navigation().displayAlertDialog(SignInActivity.this, otherUserFirstName, myUserFirstName);
                        }
                    });

                }
            }
        });
    }

    /**
     * Signs in the current user
     * @param v the view corresponding to the current screen
     */
    public void signIn(View v) {
        // Get user data
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();


        // verify all user data is entered
        if (email.length() == 0 || password.length() == 0) {
            Toast.makeText(getApplicationContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
        } else {
            // code to sign in user
            firebaseHelper.getmAuth().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Sign in successful", Toast.LENGTH_SHORT).show();
                                takeToPostSignIn(firebaseHelper.getmAuth().getCurrentUser().getUid()); // direct authenticated user to next activity
                            } else {
                                //sign in failed
                                Log.d("SignInActivity", email + " failed to log in" + task.getException());
                                Toast.makeText(getApplicationContext(), "Wrong email or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }


}
