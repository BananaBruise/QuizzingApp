package com.app.QuizzingApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Class containing some commonly used methods for navigation purposes (eg. signing out, displaying
 * an alert popup)
 */
public class Navigation {
    FirebaseHelper firebaseHelper = new FirebaseHelper();   // reference to helper class so we can sign out

    String sortPrompt = "\n(1) incorrect questions are displayed earlier than correct ones;" +
            "\n(2) questions that take you longer to answer are displayed earlier than questions answered quicker;" +
            "\n(3) questions with a higher difficulty rating are displayed earlier than questions with lower difficulty ratings.";

    /**
     * Signs a user out using firebaseHelper mAuth instance
     *
     * @param activity activity we are signing out user from
     */
    public void signOut(Activity activity) {
        firebaseHelper.getmAuth().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // if new state is null, user is signed out, so take them back to SignInActivity
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent i = new Intent(activity, SignInActivity.class);
                    activity.startActivity(i);
                }
            }
        });

        firebaseHelper.getmAuth().signOut();    // sign out user


    }

    /**
     * Displays an alert dialog on the given activity
     *
     * @param activity     activity we are displaying the popup on
     * @param teacherfName first name of teacher corresponding to current user
     * @param studentfName first name of student corresponding to current user
     */
    public void displayStartQuizDialog(Activity activity, String teacherfName, String studentfName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // set text for popup
        builder.setMessage("If your teacher (" + teacherfName + ") has posted questions, you'll " +
                        ("see them on the next page sorted by the following criteria, in decreasing order of importance:\n" +
                                sortPrompt))
                .setTitle("Get ready, " + studentfName + "!");

        // add the buttons
        builder.setPositiveButton("I'M READY NOW!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                Intent i = new Intent(activity, AnswererDashboardActivity.class);
                activity.startActivity(i);
            }
        });

        builder.setCancelable(false);

        // create & show dialog
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public void displaySortingDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // set text for popup
        builder.setMessage("Upon retry, questions are sorted, in decreasing order of importance,:\n" +
                sortPrompt);

        // add the buttons
        builder.setPositiveButton("Gotcha!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // do nothing
            }
        });

        // create & show dialog
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public void displayAboutAppDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // set text for popup
        builder.setTitle("About the app");
        builder.setMessage("This is an interactive quiz maker. A teacher makes questions for a student to answer.\n" +
                "\nAfter sign up, student will be prompted for teacher's sign up code. This allows the student to pair with teacher.\n" +
                "\nOnce paired with teacher, the student will interact with teacher's questions.");

        // add the buttons
        builder.setPositiveButton("Sign me up!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(activity, SignUpActivity.class);
                activity.startActivity(intent);
            }
        });

        // create & show dialog
        AlertDialog dialog = builder.create();

        dialog.show();
    }

}
