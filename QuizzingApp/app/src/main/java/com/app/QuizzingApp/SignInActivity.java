package com.app.QuizzingApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
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

        firebaseHelper = new FirebaseHelper();
        if (firebaseHelper.getmAuth().getCurrentUser()!=null) {
            moveToSync(firebaseHelper.getmAuth().getCurrentUser().getUid());
        }

        emailET = findViewById(R.id.signInEmailET);
        passwordET = findViewById(R.id.signInPasswordET);
    }

    public void takeToSignUp(View v) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    public void moveToSync(String uid) {
        final User[] result = new User[1];

        FirebaseHelper fbh = new FirebaseHelper();
        DocumentReference docRef = fbh.getmdb().collection("Users").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                result[0] = documentSnapshot.toObject(User.class);
                if (fbh.getmAuth().getCurrentUser()!=null){
                    // if user is not active, take them to sync screen based on role
                    if (result[0].getisActive()==false && result[0].isQuestioner() == false) {
                        startActivity(new Intent(getApplicationContext(), AnswererSyncActivity.class));
                    }
                    // otherwise is active, go to homescreen
                    else{
                        // TODO
                    }
                }
            }});
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
