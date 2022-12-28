package com.app.QuizzingApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db.collection("add1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("ho", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("ho", "Error getting documents.", task.getException());
                        }
                    }
                });

        ArrayList<String> questions = new ArrayList<String>();

        questions.add("hi");
        questions.add("bye");

        User user = new User("Abhi", "Bhashyam", questions);
        User user1 = new User("Han", "Tu", questions); // new user

        Question q = new Question("blob", "hard");

        db.collection("Users").document(user.getName()).set(user);
        db.collection("Users").document(user.getName()).set(user1);

        db.collection("Users").document(user.getName()).collection("Questions").document("Question1").set(q);
        db.collection("Users").document(user.getName()).collection("Questions").document("Question1").update("diff", "medium");

        db.collection("Users").document(user1.getName()).collection("Questions").document("Question1").set(q);
        db.collection("Users").document(user1.getName()).collection("Questions").document("Question1").update("name", "Han's question");

        // hwehaeiowa
    }
}