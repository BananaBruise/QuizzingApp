package com.app.QuizzingApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.QuizzingApp.databinding.ActivityAnswererDashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class AnswererDashboardActivity extends AppCompatActivity {

    public static FirebaseHelper firebaseHelper = new FirebaseHelper();


    ActivityAnswererDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAnswererDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String uid = firebaseHelper.getmAuth().getCurrentUser().getUid();
        firebaseHelper.readGenericUser(uid, Answerer.class, new FirebaseHelper.FirestoreCallback() {
            @Override
            public void onCallbackUser(User u) {
                Answerer answerer = (Answerer) u;
                String questionerID = answerer.getQuestionerID();
                firebaseHelper.getmdb().collection("Users").document(questionerID).collection("Questions")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    List<Question> cardList = new ArrayList<>();

                                    for (DocumentSnapshot doc : task.getResult()) {
                                        Log.i("TAG", doc.getData().toString());
                                        Question q = doc.toObject(Question.class);

                                        cardList.add(q);
                                    }

                                    Log.i("TAG", "success grabbing questions");
                                    Log.i("TAG", Integer.toString(cardList.size()));

                                    QuestionCardAdapter adapter = new QuestionCardAdapter(cardList);
                                    binding.cardStack.setLayoutManager(new CardStackLayoutManager(getApplicationContext()));
                                    binding.cardStack.setAdapter(adapter);
                                }
                            }
                        });
            }
        });
    }

    public void signOut(View v) {
        firebaseHelper.getmAuth().signOut();

        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(i);
    }

    // populate answerlistview
    // TODO: Tinder-like swiping with Binding layout
    // references:
    // binding and cardstack tutorial: https://www.youtube.com/watch?v=3HvfQ_B7-RQ
    // binding documentation: https://developer.android.com/topic/libraries/view-binding
    // recycleView doc: https://developer.android.com/develop/ui/views/layout/recyclerview
    // tinder swiping module (CardStackView): https://github.com/yuyakaido/CardStackView
}