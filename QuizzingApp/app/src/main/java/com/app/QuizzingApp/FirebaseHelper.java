package com.app.QuizzingApp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Manages all interaction with the Firestore database. All interaction with the databse will be
 * explicitly defined within this class.
 */
public class FirebaseHelper {
    // instance vars
    private FirebaseAuth mAuth; // allows authentication integration
    private FirebaseFirestore db;   // database reference
    private User currUser; // represents the user navigating activities

    // constructor
    public FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    // getters
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
        // in the Users collection, create a new document with appropriate ID
        db.collection("Users").document(getShorterString(toAdd.getUID()))
                .set(toAdd) // sets fields for this document
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
     * @param callback action taken upon returning a user; calls FirestoreUserCallback.onCallbackReadUser method
     */
    public void readUser(String uid, FirestoreUserCallback callback) {
        // get docref to the User with given uid's document
        DocumentReference docRef = db.collection("Users").document(getShorterString(uid));

        // we need to get() in order to convert to DocumentSnapshot
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            // callback; instantiates currUser to appropriate object based on whether they are Questioner or Answerer
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if ((Boolean) documentSnapshot.get("questioner")) {
                        currUser = documentSnapshot.toObject(Questioner.class);
                    } else {
                        currUser = documentSnapshot.toObject(Answerer.class);
                    }
                    callback.onCallbackReadUser(currUser);  // pass data back
                } else {
                    callback.onCallbackReadUserFail();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Generically reads a single user object from Firestore. Used when we know what type of User
     * (Answerer or Questioner) we are reading.
     *
     * @param uid       user's unique id
     * @param userClass user type to be retrieved from Firestore that extends User class e.g. Questioner or Answerer
     * @param callback  action taken upon returning a user; calls FirestoreUserCallback.onCallbackReadUser method
     * @param <T>       a user object extending User object e.g. Questioner or Answerer
     */
    public <T extends User> void readGenericUser(String uid, Class<T> userClass, FirestoreUserCallback callback) {
        // get docref to the User with given uid's document
        DocumentReference docRef = db.collection("Users").document(getShorterString(uid));

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            // callback; based on which class we are reading the object of, instantiate currUser
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currUser = documentSnapshot.toObject(userClass);
                //Log.d("FBH", currUser.getName());
                callback.onCallbackReadUser(currUser);  // pass data back
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("readGenericUser", "Error reading user");
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

        // get the colref of the collection (of Questions) corresponding to the given user's uid's document
        CollectionReference colRef = db.collection("Users").document(getShorterString(uid))
                .collection("Questions");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Question> questionsList = new ArrayList<>();
                    // for each document in the collection, make it a Question object and add it
                    // to our resulting AL
                    for (DocumentSnapshot doc : task.getResult()) {
                        questionsList.add(doc.toObject(Question.class));
                    }
                    Log.i(TAG, questionsList.toString());

                    callback.onCallbackReadQuestions(questionsList);    // pass data back
                } else {
                    Log.i(TAG, "Error reading questions");
                }
            }
        });
    }

    /**
     * Adds a specific question (uses to ID parameters to lookup documents)
     * @param uid uid of user
     * @param questionID ID of questioner so we can access their Questions collection
     * @param question new Question we are updating to
     * @param callback action taken upon adding a question; calls FirestoreQuestionCallback.onCallbackWriteQuestion method
     */
    public void writeQuestion(String uid, String questionID, Question question, FirestoreQuestionCallback callback) {
        DocumentReference docRef = db.collection("Users").document(getShorterString(uid))
                .collection("Questions").document(questionID);

        // we will use docRef from above to add the given question object to "Questions" collection
        docRef.set(question).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callback.onCallbackWriteQuestion();    // signal that we've successfully added question
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("writeQuestion", "Writing question not successful");
            }
        });
    }

    /**
     * Updates a specific question's field with the provided value
     * @param questionerID the ID of the user that has the question set
     * @param questionDocID the specific ID of the question we are updating
     * @param field the field we are updating
     * @param value the value we are changing the field to
     * @param callback action taken upon updating a Question; calls FirestoreQuestionCallback.onCallbackUpdateQuestionField method
     * @param <T> generic type of value
     */
    public <T> void updateQuestionField(String questionerID, String questionDocID, String field, T value, FirestoreQuestionCallback callback) {
        // get reference to the question doc we are updating
        DocumentReference docRef = db.collection("Users").document(getShorterString(questionerID))
                .collection("Questions").document(questionDocID);

        // update this doc's given field to the given value
        docRef.update(field, value).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callback.onCallbackUpdateQuestionField();    // signal that we've finished updating
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
     * @param callback action taken upon syncing a user; calls FirestoreUserCallback.onCallbackUserSync/
     *                 onCallbackUserSyncFail method
     */
    public void syncUsers(String otherUid, String myUid, FirestoreUserCallback callback) {
        String TAG = "syncUsers";

        // docref to OTHER user (Questioner)
        DocumentReference otherDocRef = db.collection("Users").document(getShorterString(otherUid));

        otherDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot otherUserDoc) {
                // if doc with otherUid exists
                if (otherUserDoc.exists()) {

                    // updating my user's isActive status
                    DocumentReference myDocRef = db.collection("Users").document(getShorterString(myUid));
                    myDocRef.update("isActive", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Answerer's isActive successfully updated!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Answerer's isActive NOT successfully updated");
                        }
                    });

                    // updating my user's questionerID (used to lookup question set)
                    myDocRef.update("questionerID", getShorterString(otherUid))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "questionerID successfully updated!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "questionerID NOT successfully updated");
                        }
                    });

                    // update other user's isActive
                    otherDocRef.update("isActive", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Questioner's isActive successfully updated!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Questioner's isActive NOT successfully updated");
                        }
                    });

                    // get my name from doc ref
                    myDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot myDocSnapshot) {
                            // pass my user's and other user's first names back
                            callback.onCallbackUserSync(otherUserDoc.get("fName").toString(),
                                    myDocSnapshot.get("fName").toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Sync not successful");
                        }
                    });
                } else {    // if doc didn't exist, UID was mistyped, so signal this by calling
                            // this callback
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
     * @param callback action taken upon returning name pair; calls FirestoreUserCallback.onCallbackUserSync method
     */
    public void readSyncedPair(String questionerUid, String answererUid, FirestoreUserCallback callback) {
        // get docref to the Answerer's doc
        DocumentReference answererDocRef = db.collection("Users").document(getShorterString(answererUid));

        answererDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshotAnswerer) {
                // get docref to Questioner's doc
                DocumentReference questionerDocRef = db.collection("Users").document(getShorterString(questionerUid));

                questionerDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshotQuestioner) {
                        callback.onCallbackUserSync(documentSnapshotQuestioner.get("fName").toString(),
                                documentSnapshotAnswerer.get("fName").toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("readSyncedPair", "readSyncedPair not successful");
            }
        });
    }

    /**
     * Takes in a given String and shortens it to 10 characters
     * @param s given string we are shortening
     * @return a shortened version of the given String
     */
    public static String getShorterString(String s) {
        return s.substring(0, 10);  // so we can have shorter UIDs
    }

    // interfaces
    /**
     * Callback interface for FirebaseHelper utility methods. This is needed bc firebase tasks are async
     */
    public interface FirestoreUserCallback {
        default void onCallbackReadUser(User u) {
        }

        default void onCallbackReadUserFail() {
        }

        default void onCallbackUserSync(String otherUserFirstName, String myUserFirstName) {
        }

        default void onCallbackUserSyncFail() {
        }



    }

    public interface FirestoreQuestionCallback {
        default void onCallbackReadQuestions(ArrayList<Question> questionList) {
        }

        default void onCallbackWriteQuestion() {
        }

        default void onCallbackUpdateQuestionField() {
        }
    }
}
