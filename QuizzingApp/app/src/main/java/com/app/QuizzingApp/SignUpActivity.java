package com.app.QuizzingApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

/**
 * Used to sign a user up and add them to the database
 */
public class SignUpActivity extends AppCompatActivity {

    // references to UI elements
    private EditText emailET, passwordET, firstNameET, lastNameET;
    private Switch isStudentSwitch;

    FirebaseHelper firebaseHelper = new FirebaseHelper();   // reference to helper class

    private final String TAG = "SignUpActivity";


    /**
     * Instantiate UI references
     * @param savedInstanceState may be used to restore activity to a previous state
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // instantiate UI references
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        firstNameET = findViewById(R.id.firstNameET);
        lastNameET = findViewById(R.id.lastNameET);
        isStudentSwitch = findViewById(R.id.isStudentSW);

    }

    /**
     * Signs up the current user and adds them to the Firestore database (using FirebaseHelper class)
     * @param v view corresponding to current screen
     */
    public void signUp(View v) {
        // get fields
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String firstName = firstNameET.getText().toString();
        String lastName = lastNameET.getText().toString();
        boolean isQuestioner = !isStudentSwitch.isChecked();

        try {
            // use mAuth reference to create user (authenticate)
            firebaseHelper.getmAuth().createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // user account was created in firebase auth
                        Log.i(TAG, email + " account created");

                        FirebaseUser user = firebaseHelper.getmAuth().getCurrentUser();

                        // based on if the user is a Questioner or Answerer, add them to Firestore
                        if (isQuestioner){
                            firebaseHelper.addGenericUserToFirestore(new Questioner(firstName, lastName, FirebaseHelper.getShorterString(user.getUid()), email, password));
                        } else {
                            firebaseHelper.addGenericUserToFirestore(new Answerer(firstName, lastName, FirebaseHelper.getShorterString(user.getUid()), email, password));
                        }

                        // successful sign up
                        Toast.makeText(getApplicationContext(), "Welcome, " + firstName + "!", Toast.LENGTH_SHORT).show();
                        takeToPostSignUp(user.getUid());
                    } else {
                        // user WASN'T created
                        Log.d(TAG, email + " sign up failed");
                        Toast.makeText(getApplicationContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            // exception thrown means an error occured
            Toast.makeText(getApplicationContext(), "An error occured", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Takes user to appropriate screen after they have signed up
     * @param uid uid of current user
     */
    public void takeToPostSignUp(String uid) {
        firebaseHelper.readUser(uid, new FirebaseHelper.FirestoreUserCallback() {
            @Override
            public void onCallbackReadUser(User u) {
                // if the signed up user is an Answerer
                if (u.isQuestioner() == false) {
                    // take to AnswererSyncActivity
                    startActivity(new Intent(getApplicationContext(), AnswererSyncActivity.class));
                } else if (u.isQuestioner() == true) {
                    // otherwise take to QuestionerSyncActivity
                    startActivity(new Intent(getApplicationContext(), QuestionerSyncActivity.class));
                }
            }
        });
    }
}