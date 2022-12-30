package com.app.QuizzingApp;

import android.util.Log;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;

public class FirebaseHelper {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public FirebaseAuth getmAuth()
    {
        return mAuth;
    }

    public void addUserToFirestore(String firstName, String lastName, String uid, String email, String password, boolean isQuestioner)
    {
        User toAdd = new User(firstName, lastName, uid, email, password, isQuestioner);

        db.collection("Users").document(uid)
                .set(toAdd)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("TAG", "account added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Error adding account", e);
                    }
                });

    }

    public User getUser(String uid) {
        final User[] result = new User[1];

        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                result[0] = documentSnapshot.toObject(User.class);
            }
        });

        return result[0];
    }
}
