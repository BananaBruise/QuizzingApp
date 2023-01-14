package com.app.QuizzingApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.app.QuizzingApp.databinding.ActivityAnswererDashboardBinding;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.util.ArrayList;
import java.util.List;

public class AnswererDashboardActivity extends AppCompatActivity implements CardStackListener {

    // global variables
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    CheckBox correct1CB, correct2CB, correct3CB, correct4CB;

    String questionerID;

    List<Question> cardList = new ArrayList<>();

    ActivityAnswererDashboardBinding binding;

    CardStackLayoutManager manager;

    Toast m_currentToast = null;

    ArrayList<Question> wrong = new ArrayList<>();



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
        firebaseHelper.readGenericUser(uid, Answerer.class, new FirebaseHelper.FirestoreUserCallback() {
            @Override
            public void onCallbackUser(User u) {
                Answerer answerer = (Answerer) u;
                questionerID = answerer.getQuestionerID();
                firebaseHelper.readQuestions(questionerID, new FirebaseHelper.FirestoreQuestionCallback() {
                    @Override
                    public void onCallbackQuestions(ArrayList<Question> questionList) {
                        cardList = questionList;

                        // put the card list in the view
                        QuestionCardAdapter adapter = new QuestionCardAdapter(cardList);
                        binding.cardStack.setLayoutManager(manager);
                        binding.cardStack.setAdapter(adapter);
                    }
                });

            }
        });
    }

    public void signOut(View v) {
         new Navigation().signOut(AnswererDashboardActivity.this);
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {
        // unused
    }

    @Override
    public void onCardSwiped(Direction direction) {
        // unused
    }

    @Override
    public void onCardRewound() {
        // unused
    }

    @Override
    public void onCardCanceled() {
        // unused
    }

    @Override
    public void onCardAppeared(View view, int position) {
        // unused
    }

    @Override
    public void onCardDisappeared(View view, int position) {
        // get references to UI elements
        correct1CB = view.findViewById(R.id.cor1CB);
        correct2CB = view.findViewById(R.id.cor2CB);
        correct3CB = view.findViewById(R.id.cor3CB);
        correct4CB = view.findViewById(R.id.cor4CB);

        // user answers
        List<Boolean> entered = new ArrayList<>();
        entered.add(correct1CB.isChecked());
        entered.add(correct2CB.isChecked());
        entered.add(correct3CB.isChecked());
        entered.add(correct4CB.isChecked());

        // "correct" answers
        List<Answer> answerList = cardList.get(position).getAnswers();

        // question doc ID
        String questionDocID = Integer.toString(cardList.get(position).getQuestionID());

        Boolean isCorrect = true;

        for (int i = 0; i < entered.size(); i++) {
            if (entered.get(i) != answerList.get(i).getCorrect()) {
                isCorrect = false;
                wrong.add(cardList.get(position));
                Log.i("onCardDisappeared", "Question was incorrect!");
                break;
            }
        }

        // update question status
        firebaseHelper.updateQuestionField(questionerID, questionDocID, "correctlyAnsweredLastTime", isCorrect, new FirebaseHelper.FirestoreQuestionCallback() {
            @Override
            public void onCallbackQuestionUpdate() {
                if (position == cardList.size() - 1) {
                    Toast.makeText(getApplicationContext(), "You finished the quiz!", Toast.LENGTH_SHORT).show();
                    takeToViewQuestion(wrong);
                }
            }
        });
    }

    public void takeToViewQuestion(ArrayList<Question> wrongQuestions) {
        Intent i = new Intent(getApplicationContext(), PostQuizActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("WRONGS", wrongQuestions);
        i.putExtra("INCORRECT_QUESTIONS", bundle);
        startActivity(i);

    }

}