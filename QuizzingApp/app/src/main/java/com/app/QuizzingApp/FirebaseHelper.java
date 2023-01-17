package com.app.QuizzingApp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Manages all interaction with the Firestore database. All interaction with the databse will be
 * explicitly defined within this class.
 */
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
    /**
     * Returns FirebaseAuth instance, allowing for authentication
     * @return FirebaseAuth instance, allowing for authentication
     */
    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    /**
     * Returns a FirebaseFirestore instance, allowing for interaction with database
     * @return FirebaseFirestore instance, allowing for interaction with database
     */
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
        db.collection("Users").document(getShorterString(toAdd.getUID()))
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
     * Reads a single user object from Firestore. Used when its not clear what type of User
     * (Answerer or Questioner) we are reading.
     *
     * @param uid      user's unique id
     * @param callback action taken upon returning a user; calls FirestoreUserCallback.onCallbackUser method
     */
    public void readUser(String uid, FirestoreUserCallback callback) {
        DocumentReference docRef = db.collection("Users").document(getShorterString(uid));
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if ((Boolean) documentSnapshot.get("questioner")) {
                    currUser = documentSnapshot.toObject(Questioner.class);
                } else {
                    currUser = documentSnapshot.toObject(Answerer.class);
                }
                callback.onCallbackUser(currUser);
            }
        });
    }

    /**
     * Generically reads a single user object from Firestore. Used when we know what type of User
     * (Answerer or Questioner) we are reading.
     *
     * @param uid       user's unique id
     * @param userClass user type to be retrieved from Firestore that extends User class e.g. Questioner or Answerer
     * @param callback  action taken upon returning a user; calls FirestoreUserCallback.onCallbackUser method
     * @param <T>       a user object extending User object e.g. Questioner or Answerer
     */
    public <T extends User> void readGenericUser(String uid, Class<T> userClass, FirestoreUserCallback callback) {
        DocumentReference docRef = db.collection("Users").document(getShorterString(uid));
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
        CollectionReference colRef = db.collection("Users").document(getShorterString(uid)).collection("Questions");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Question> questionsList = new ArrayList<>();
                    for (DocumentSnapshot doc : task.getResult()) {
                        //Log.i(TAG, doc.getData().toString());
                        questionsList.add(doc.toObject(Question.class));
                        //Log.i(TAG, doc.toObject(Question.class).toString());
                    }
                    //Log.i(TAG, "success grabbing questions");
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
        DocumentReference docRef = db.collection("Users").document(getShorterString(uid)).collection("Questions").document(questionID);

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
        DocumentReference docRef = db.collection("Users").document(getShorterString(questionerID)).collection("Questions").document(questionDocID);

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
     * @param callback action taken upon syncing a user; calls FirestoreUserCallback.onCallbackUserSyncNamePair/
     *                 onCallbackUserSyncFail method
     */
    public void syncUsers(String otherUid, String myUid, FirestoreUserCallback callback) {
        String TAG = "syncUsers";

        DocumentReference otherDocRef = db.collection("Users").document(getShorterString(otherUid));

        otherDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot otherUserDoc) {
                if (otherUserDoc.exists()) {
                    // updating my user's isActive status
                    DocumentReference myDocRef = db.collection("Users").document(getShorterString(myUid));
                    myDocRef.update("isActive", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Answerer's isActive successfully updated!");
                        }
                    });
                    // updating my user's questionerID (used to lookup question set)
                    myDocRef.update("questionerID",getShorterString(otherUid)).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    // get my name from doc snapshot of doc ref
                    myDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            callback.onCallbackUserSyncNamePair(otherUserDoc.get("fName").toString(), value.get("fName").toString());
                        }
                    });
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

    /**
     * Gives us the first name of the Answerer and Questioner corresponding to the given
     * questionerUid and answererUid
     * @param questionerUid uid of Questioner we are finding the name of
     * @param answererUid uid of Answerer we are finding the name of
     * @param callback action taken upon returning name pair; calls FirestoreUserCallback.onCallbackUserSyncNamePair method
     */
    public void readSyncedPair(String questionerUid, String answererUid, FirestoreUserCallback callback) {
        DocumentReference answererDocRef = db.collection("Users").document(getShorterString(answererUid));

        answererDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DocumentReference questionerDocRef = db.collection("Users").document(getShorterString(questionerUid));

                questionerDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        callback.onCallbackUserSyncNamePair(value.get("fName").toString(), documentSnapshot.get("fName").toString());
                    }
                });
            }
        });
    }

    /**
     * Takes in a given String and shortens it to 10 characters
     * @param s given string we are shortening
     * @return a shortened version of the given String
     */
    public static String getShorterString(String s) {
        return s.substring(0, 10);
    }

    // interfaces

    /**
     * Callback interface for FirebaseHelper utility methods. This is needed bc firebase tasks are async
     */
    public interface FirestoreUserCallback {
        default void onCallbackUser(User u) {
        }

        ;

        default void onCallbackUserSyncNamePair(String otherUserFirstName, String myUserFirstName) {
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
