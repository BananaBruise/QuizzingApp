package com.app.QuizzingApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.app.QuizzingApp.databinding.ActivityAnswererDashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.util.ArrayList;
import java.util.List;

public class AnswererDashboardActivity extends AppCompatActivity implements CardStackListener {

    public static FirebaseHelper firebaseHelper = new FirebaseHelper();

    CheckBox correct1CB, correct2CB, correct3CB, correct4CB;

    String questionerID;

    List<Question> cardList = new ArrayList<>();

    ActivityAnswererDashboardBinding binding;

    CardStackLayoutManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // grab layout (binding) of this activity
        binding = ActivityAnswererDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // create manager object for cardstack (first arg = context, second arg = listener interface)
        manager = new CardStackLayoutManager(this, this);
        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(5);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollVertical(false);

        // populate stack of questions for answerer
        String uid = firebaseHelper.getmAuth().getCurrentUser().getUid();
        firebaseHelper.readGenericUser(uid, Answerer.class, new FirebaseHelper.FirestoreCallback() {
            @Override
            public void onCallbackUser(User u) {
                Answerer answerer = (Answerer) u;
                questionerID = answerer.getQuestionerID();
                firebaseHelper.getmdb().collection("Users").document(questionerID).collection("Questions")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {


                                    for (DocumentSnapshot doc : task.getResult()) {
                                        Log.i("TAG", doc.getData().toString());
                                        Question q = doc.toObject(Question.class);

                                        cardList.add(q);
                                    }

                                    Log.i("TAG", "success grabbing questions");
                                    Log.i("TAG", Integer.toString(cardList.size()));

                                    QuestionCardAdapter adapter = new QuestionCardAdapter(cardList);
                                    binding.cardStack.setLayoutManager(manager);
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

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {

    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {
        correct1CB = view.findViewById(R.id.cor1);
        correct2CB = view.findViewById(R.id.cor2);
        correct3CB = view.findViewById(R.id.cor3);
        correct4CB = view.findViewById(R.id.cor4);
        // user answers
        Log.i("TAG", "dissappeared");
        List<Boolean> entered = new ArrayList<>();
        entered.add(correct1CB.isChecked());
        entered.add(correct2CB.isChecked());
        entered.add(correct3CB.isChecked());
        entered.add(correct4CB.isChecked());

        // actual answers
        List<Answer> answerList = cardList.get(position).getAnswers();

        for (int i = 0; i < entered.size(); i++) {
            if (entered.get(i) != answerList.get(i).getCorrect()) {
                Log.i("if", "in if");
                firebaseHelper.getmdb().collection("Users").document(questionerID).collection("Questions")
                        .document(Integer.toString(cardList.get(position).getQuestionID()))
                        .update("correctlyAnsweredLastTime", false)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Question was INCORRECT!", Toast.LENGTH_SHORT).show();
                                Log.i("onSuccess", "Question was incorrect");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("failed", "onfailure");
                            }
                        });
                return;

            }
        }

        firebaseHelper.getmdb().collection("Users").document(questionerID).collection("Questions")
                .document(Integer.toString(cardList.get(position).getQuestionID()))
                .update("correctlyAnsweredLastTime", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Question was CORRECT!", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    // populate answerlistview
    // TODO: Tinder-like swiping with Binding layout
    // references:
    // binding and cardstack tutorial: https://www.youtube.com/watch?v=3HvfQ_B7-RQ
    // binding documentation: https://developer.android.com/topic/libraries/view-binding
    // recycleView doc: https://developer.android.com/develop/ui/views/layout/recyclerview
    // tinder swiping module (CardStackView): https://github.com/yuyakaido/CardStackView
}