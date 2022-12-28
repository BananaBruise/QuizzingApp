package com.app.QuizzingApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class SignUpActivity extends AppCompatActivity {

    private EditText emailET, passwordET, firstNameET, lastNameET;
    private Switch isStudentSwitch;

    public static FirebaseHelper firebaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        firstNameET = findViewById(R.id.firstNameET);
        lastNameET = findViewById(R.id.lastNameET);
        isStudentSwitch = findViewById(R.id.isStudentID);

        firebaseHelper = new FirebaseHelper();
    }
    public void signUp(View v) {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        Log.w("TAG", email + password);
        String firstName = firstNameET.getText().toString();
        String lastName = lastNameET.getText().toString();
        boolean isQuestioner = !isStudentSwitch.isChecked();

        try {
            firebaseHelper.getmAuth().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // user account was created in firebase auth
                                Log.i("TAG", email + " account created");

                                FirebaseUser user = firebaseHelper.getmAuth().getCurrentUser();

                                firebaseHelper.addUserToFirestore(firstName, lastName, user.getUid(), email, password, isQuestioner);

                                Toast.makeText(getApplicationContext(), "Welcome, " + firstName + "!", Toast.LENGTH_SHORT).show();
//                            // choose whatever actions you want - update UI, switch to a new screen, etc.
//                            // take the user to the screen where they can enter their wishlist items
//                            // get application context will get the activity we are currently in that
//                            // is sending the intent. Similar to how we have said "this" in the past
//
//                            Intent intent = new Intent(getApplicationContext(), bVolViewProfile.class);
//                            startActivity(intent);
                            } else {
                                // user WASN'T created
                                Log.d("TAG", email + " sign up failed");
                                Toast.makeText(getApplicationContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "An error occured", Toast.LENGTH_SHORT).show();
        }

    }

}