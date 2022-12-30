package com.app.QuizzingApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class AnswererSyncActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answerer_sync);
    }

    public void syncAnswerer(View v){
        // lookup UID

        // match the UID
        // if match --> update both questioner and answerer

        // if not match --> toast of failed sync
    }
}