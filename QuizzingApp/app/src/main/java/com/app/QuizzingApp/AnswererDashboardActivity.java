package com.app.QuizzingApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        List<Card> cards = new ArrayList<>();
        cards.add(new Card("a", "diff: 4", "a", "b", "c", "d"));
        cards.add(new Card("b", "diff: 4", "a", "b", "c", "d"));
        cards.add(new Card("c", "diff: 4", "a", "b", "c", "d"));
        cards.add(new Card("d", "diff: 4", "a", "b", "c", "d"));
        cards.add(new Card("e", "diff: 4", "a", "b", "c", "d"));

        CardAdapter adapter = new CardAdapter(cards);
        binding.cardStack.setLayoutManager(new CardStackLayoutManager(getApplicationContext()));
        binding.cardStack.setAdapter(adapter);
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