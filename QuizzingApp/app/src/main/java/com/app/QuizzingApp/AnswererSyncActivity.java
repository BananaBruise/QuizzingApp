package com.app.QuizzingApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity that manages the syncing of an Answerer to a Questioner
 */
public class AnswererSyncActivity extends AppCompatActivity {
    // global variables
    FirebaseHelper firebaseHelper = new FirebaseHelper();   // reference to our helper class

    EditText codeET;    // EditText for code (will be updated later)

    /**
     * Instantiate references to views
     * @param savedInstanceState may be used to restore activity to a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answerer_sync);

        codeET = findViewById(R.id.syncCodeET); // instantiate reference to code ET
    }

    /**
     * Syncs an Answerer to a Questioner
     * @param v the view corresponding to the current screen
     */
    public void syncAnswerer(View v) {
        // the questioner's UID (will be used for lookup later)
        String otherUid = codeET.getText().toString().trim();

        if (otherUid.length() != 10) {
            Toast.makeText(getApplicationContext(), "Code must be 10 characters long!", Toast.LENGTH_SHORT).show();
        } else {

            // my UID (used in Firebase method for syncing purposes)
            String myUid = firebaseHelper.getmAuth().getCurrentUser().getUid();

            firebaseHelper.readUser(otherUid, new FirebaseHelper.FirestoreUserCallback() {
                @Override
                public void onCallbackReadUser(User u) {
                    if (u.isQuestioner()) {
                        if (!u.getisActive()) {
                            // callback used to sync two users
                            firebaseHelper.syncUsers(otherUid, myUid, new FirebaseHelper.FirestoreUserCallback() {

                                // callback; if sync is successful, display message and alert informing Answerer of what's to come
                                @Override
                                public void onCallbackUserSync(String otherUserFirstName, String myUserFirstName) {
                                    Toast.makeText(getApplicationContext(), "You are synced to " +
                                            otherUserFirstName, Toast.LENGTH_SHORT).show();
                                    new Navigation().displayStartQuizDialog(AnswererSyncActivity.this,
                                            otherUserFirstName, myUserFirstName);
                                }

                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "This teacher is already synced " +
                                    "with another student.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "You can't sync with another " +
                                "student.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCallbackReadUserFail() {
                    Toast.makeText(getApplicationContext(), "You entered an invalid code",
                            Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

    /**
     * Signs out a user
     * @param v the view corresponding to the current screen
     */
    public void signOut(View v) {
        new Navigation().signOut(AnswererSyncActivity.this);
    }
}