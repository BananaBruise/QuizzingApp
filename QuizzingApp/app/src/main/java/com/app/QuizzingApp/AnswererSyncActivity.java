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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class AnswererSyncActivity extends AppCompatActivity {

    FirebaseHelper firebaseHelper = new FirebaseHelper();

    EditText codeET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answerer_sync);

        codeET = findViewById(R.id.SyncCodeET);
    }

    public void syncAnswerer(View v){
        String otherUid = codeET.getText().toString();

        DocumentReference docRef = firebaseHelper.getmdb().collection("Users").document(otherUid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult(); // getting otherUser record
                    if (document.exists()) {
                        // my user is active
                        firebaseHelper.getmdb().collection("Users").document(firebaseHelper.getmAuth().getCurrentUser().getUid()).update("isActive", true);
                        // other user is active
                        docRef.update("isActive", true);
                        docRef.update("questionID",document.get("UID")); // otherUser's UID becomes how we look up question set (questionID)
                        // log
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        // toast
                        Toast.makeText(getApplicationContext(), "You are synced to " + document.get("fName"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "You entered an invalid code", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    public void signOut(View v) {
        firebaseHelper.getmAuth().signOut();

        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(i);
    }
}