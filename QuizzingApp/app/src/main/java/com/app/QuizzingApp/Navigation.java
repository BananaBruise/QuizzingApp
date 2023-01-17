package com.app.QuizzingApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Class containing some commonly used methods for navigation purposes (eg. signing out, displaying
 * an alert popup)
 */
public class Navigation {
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    /**
     * Signs a user out using firebaseHelper mAuth instance
     * @param activity activity we are signing out user from
     */
    public void signOut(Activity activity) {
        firebaseHelper.getmAuth().signOut();
        Intent i = new Intent(activity, SignInActivity.class);
        activity.startActivity(i);
    }

    /**
     * Displays an alert dialog on the given activity
     * @param activity activity we are displaying the popup on
     * @param teacherfName first name of teacher corresponding to current user
     * @param studentfName first name of student corresponding to current user
     */
    public void displayAlertDialog(Activity activity, String teacherfName, String studentfName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage("If your teacher (" + teacherfName + ") has posted questions, you'll " +
                        "see a quiz on the next page! If you're not ready, close this pop-up and start later!")
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

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Toast.makeText(activity.getApplicationContext(), "Start when you're ready!", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

}
