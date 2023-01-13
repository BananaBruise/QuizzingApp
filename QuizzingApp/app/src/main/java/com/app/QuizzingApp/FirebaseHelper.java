package com.app.QuizzingApp;

import android.util.Log;

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

    /**
     * Reads a specific collection of questions (uses to uid parameter to lookup document)
     * @param uid uid of user
     * @param callback action taken upon returning a question set; calls FirestoreQuestionCallback.onCallbackQuestion method
     */
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

                    callback.onCallbackQuestions(questionsList);
                }
            }
        });
    }

    /**
     * Adds a specific question (uses to ID parameters to lookup documents)
     * @param uid uid of user
     * @param questionID ID of questioner so we can access their Questions collection
     * @param question new Question we are upadting to
     * @param callback action taken upon adding a question; calls FirestoreQuestionCallback.onCallbackQuestionWriter method
     */
    public void writeQuestion(String uid, String questionID, Question question, FirestoreQuestionCallback callback) {
        DocumentReference docRef = db.collection("Users").document(uid).collection("Questions").document(questionID);

        docRef.set(question).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callback.onCallbackQuestionWriter();
            }
        });
    }

    /**
     * Updates a specific question's field with the provided value
     * @param questionerID the ID of the user that has the question set
     * @param questionDocID the specific ID of the question we are updating
     * @param field the field we are updating
     * @param value the value we are changing the field to
     * @param callback action taken upon updating a Question; calls FirestoreQuestionCallback.onCallbackQuestionUpdate method
     * @param <T> generic type of value
     */
    public <T> void updateQuestionField(String questionerID, String questionDocID, String field, T value, FirestoreQuestionCallback callback) {
        DocumentReference docRef = db.collection("Users").document(questionerID).collection("Questions").document(questionDocID);

        docRef.update(field, value).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callback.onCallbackQuestionUpdate();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Syncs two users (Answerer and Questioner)
     * @param otherUid the ID of the questioner we are syncing with
     * @param myUid my user's ID
     * @param callback action taken upon syncing a user; calls FirestoreUserCallback.onCallbackUserSyncSuccess/Fail method
     */
    public void syncUsers(String otherUid, String myUid, FirestoreUserCallback callback) {
        String TAG = "syncUsers";

        DocumentReference otherDocRef = db.collection("Users").document(otherUid);

        otherDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot otherUserDoc) {
                if (otherUserDoc.exists()) {
                    // updating my user's isActive status
                    DocumentReference myDocRef = db.collection("Users").document(myUid);
                    myDocRef.update("isActive", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Answerer's isActive successfully updated!");
                        }
                    });
                    // updating my user's questionerID (used to lookup question set)
                    myDocRef.update("questionerID",otherUid).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "questionID successfully updated!");
                        }
                    });
                    // update other user's isActive
                    otherDocRef.update("isActive", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Questioner's isActive successfully updated!");
                        }
                    });

                    callback.onCallbackUserSyncSuccess(otherUserDoc.get("fName").toString());
                } else {
                    callback.onCallbackUserSyncFail();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "task not successful", e);
            }
        });
    }

    // interfaces

    /**
     * Callback interface for FirebaseHelper utility methods. This is needed bc firebase tasks are async
     */
    public interface FirestoreUserCallback {
        default void onCallbackUser(User u) {
        }

        ;

        default void onCallbackUserSyncSuccess(String otherUserFirstName) {
        }

        ;

        default void onCallbackUserSyncFail() {
        }

        ;


    }

    public interface FirestoreQuestionCallback {
        default void onCallbackQuestions(ArrayList<Question> questionList) {
        }

        ;

        default void onCallbackQuestionWriter() {
        }

        ;

        default void onCallbackQuestionUpdate() {
        }

        ;
    }
}
