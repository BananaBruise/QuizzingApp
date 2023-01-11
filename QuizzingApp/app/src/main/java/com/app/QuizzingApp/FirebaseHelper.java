package com.app.QuizzingApp;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FirebaseHelper {
    // instance var
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private User currUser; // represent the user navigating activities

    // constructor
    public FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    // getter
    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public FirebaseFirestore getmdb() {
        return db;
    }

    // utility methods

    /**
     * Adds a single user to Firestore
     *
     * @param firstName    user's first name
     * @param lastName     user's last name
     * @param uid          user's unique id
     * @param email        user's email
     * @param password     user's password
     * @param isQuestioner whether this user is a teacher (true) or student (false)
     */
    public void addUserToFirestore(String firstName, String lastName, String uid, String email, String password, boolean isQuestioner) {
        User toAdd = new User(firstName, lastName, uid, email, password, isQuestioner);

        String TAG = "addUserToFirestore";
        db.collection("Users").document(uid)
                .set(toAdd)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(TAG, "account added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error adding account", e);
                    }
                });
    }

    /**
     * Generically adds a single user to Firestore. For example, you may pass a Questioner object into this method
     *
     * @param toAdd the user to be added
     * @param <T>   a user object extending User object e.g. Questioner or Answerer
     */
    public <T extends User> void addGenericUserToFirestore(T toAdd) {
        String TAG = "addGenericUserToFirestore";
        db.collection("Users").document(toAdd.getUID())
                .set(toAdd)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(TAG, "account added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error adding account", e);
                    }
                });
    }

    /**
     * Reads a single user object from Firestore
     *
     * @param uid      user's unique id
     * @param callback action taken upon returning a user; calls FirestoreUserCallback.onCallbackUser method
     */
    public void readUser(String uid, FirestoreUserCallback callback) {
        DocumentReference docRef = db.collection("Users").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currUser = documentSnapshot.toObject(User.class);
                //Log.d("FBH", currUser.getName());
                callback.onCallbackUser(currUser);
            }
        });
    }

    /**
     * Generically reads a single user object from Firestore
     *
     * @param uid       user's unique id
     * @param userClass user type to be retrieved from Firestore that extends User class e.g. Questioner or Answerer
     * @param callback  action taken upon returning a user; calls FirestoreUserCallback.onCallbackUser method
     * @param <T>       a user object extending User object e.g. Questioner or Answerer
     */
    public <T extends User> void readGenericUser(String uid, Class<T> userClass, FirestoreUserCallback callback) {
        DocumentReference docRef = db.collection("Users").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currUser = documentSnapshot.toObject(userClass);
                //Log.d("FBH", currUser.getName());
                callback.onCallbackUser(currUser);
            }
        });
    }

    public void readQuestions(String uid, FirestoreQuestionCallback callback) {
        String TAG = "readQuestions";
        CollectionReference colRef = db.collection("Users").document(uid).collection("Questions");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Question> questionsList = new ArrayList<>();
                    for (DocumentSnapshot doc : task.getResult()) {
                        Log.i(TAG, doc.getData().toString());
                        questionsList.add(doc.toObject(Question.class));
                    }
                    Log.i(TAG, "success grabbing questions");
                    Log.i(TAG, questionsList.toString());

                    callback.onCallbackQuestion(questionsList);
                }
            }
        });
    }

    public void writeQuestion(String uid, String questionID, Question question, FirestoreQuestionCallback callback) {
        DocumentReference docRef = db.collection("Users").document(uid).collection("Questions").document(questionID);

        docRef.set(question).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callback.onCallbackQuestionWriter();
            }
        });
    }

    // interfaces

    /**
     * Callback interface for FirebaseHelper utility methods. This is needed bc firebase tasks are async
     */
    public interface FirestoreUserCallback {
        void onCallbackUser(User u);
    }

    public interface FirestoreQuestionCallback {
        default void onCallbackQuestion(ArrayList<Question> questionList) {
        }

        ;

        default void onCallbackQuestionWriter() {
        }

        ;
    }
}
