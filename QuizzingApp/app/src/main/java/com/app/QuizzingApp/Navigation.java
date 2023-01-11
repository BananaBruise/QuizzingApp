package com.app.QuizzingApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Navigation extends Activity{
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    public void signOut(Context c) {
        firebaseHelper.getmAuth().signOut();
        Intent i = new Intent(c, SignInActivity.class);
        startActivity(i);
    }
}
