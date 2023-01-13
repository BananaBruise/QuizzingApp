package com.app.QuizzingApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Navigation {
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    public void signOut(Activity activity) {
        firebaseHelper.getmAuth().signOut();
        Intent i = new Intent(activity, SignInActivity.class);
        activity.startActivity(i);
    }

}
