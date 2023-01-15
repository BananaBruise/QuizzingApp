package com.app.QuizzingApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.app.QuizzingApp.databinding.ActivityAnswererDashboardBinding;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class AnswererDashboardActivity extends AppCompatActivity implements CardStackListener {

    // global variables
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    CheckBox correct1CB, correct2CB, correct3CB, correct4CB;

    String questionerID;

    List<Question> cardList = new ArrayList<>();

    ActivityAnswererDashboardBinding binding;

    CardStackLayoutManager manager;

    ArrayList<Question> wrong = new ArrayList<>();

    QuestionCardAdapter adapter;

    CountDownTimer cdt;

    // timer TV on each card that we'll be updating
    TextView cardTimerTV;

    long millisElapsedForQuestion;

    // timer variables
    long totalSeconds = 10;
    long intervalSeconds = 1;

    // formatting tools
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("mm:ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // grab layout (binding) of this activity
        binding = ActivityAnswererDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // create manager object for cardstack (first arg = context, second arg = listener interface)
        manager = new CardStackLayoutManager(this, this);
        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(3);
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
                        cardList = heapSort(questionList);

                        // put the card list in the view (supply current binding)
                        adapter = new QuestionCardAdapter(cardList);
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
        // get reference to timerTV
        cardTimerTV = view.findViewById(R.id.timerTV);

        Animation a = AnimationUtils.loadAnimation(this, R.anim.blink);


        cdt = new CountDownTimer(totalSeconds * 1000, intervalSeconds * 1000) {

            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished <= ((5 + 1) * 1000)) {
                    cardTimerTV.setTextColor(Color.RED);
                    cardTimerTV.startAnimation(a);
                } else {
                    cardTimerTV.setTextColor(getColor(R.color.green));
                }

                cardTimerTV.setText(mSimpleDateFormat.format(millisUntilFinished));


                millisElapsedForQuestion = (totalSeconds * 1000) - millisUntilFinished;
            }

            public void onFinish() {
                cardTimerTV.setText(mSimpleDateFormat.format(0));
                binding.cardStack.swipe();
                Toast.makeText(getApplicationContext(), "You took the full time!", Toast.LENGTH_SHORT).show();
            }
        };

        cdt.start();

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
        Log.i("card list size", Integer.toString(cardList.size()));
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

        // update time taken for card
        firebaseHelper.updateQuestionField(questionerID, questionDocID, "millisElapsedToAnswer", (Long) millisElapsedForQuestion, new FirebaseHelper.FirestoreQuestionCallback() {
            @Override
            public void onCallbackQuestionUpdate() {
                Log.i("onCardDisappeared", "updated time stamp!");
            }
        });

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

        cdt.cancel();

    }

    public void takeToViewQuestion(ArrayList<Question> wrongQuestions) {
        Intent i = new Intent(getApplicationContext(), PostQuizActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("WRONGS", wrongQuestions);
        i.putExtra("INCORRECT_QUESTIONS", bundle);
        startActivity(i);

    }

    // HEAP CODE (dequeue() and percolateDown())
    private ArrayList<Question> heapSort(List<Question> questions) {
        // heapify
        heapify(questions);

        Log.i("after heapify", questions.toString());

        // sort
        int arraySize = questions.size();

        ArrayList<Question> result = new ArrayList<>(); // copy

        for (int i = 0; i < arraySize; i++) {
            result.add(dequeue(questions));
        }

        Log.i("after sort", result.toString());

        return result;
    }

    public void heapify(List<Question> questions) {
        // empty or 1 item heap is already a heap
        if (questions.isEmpty() || questions.size() == 1)
            return;

        // heapify starting from last internal node
        this.heapifyHelper((questions.size() - 2) / 2, questions);
    }

    protected void heapifyHelper(int index, List<Question> questions) {
        // internal node index is valid all the way back to root
        if (index >= 0) {
            // percolate down to make heap subtree valid
            percolateDown(index, questions);
            // heapify the previous internal node
            heapifyHelper(index - 1, questions);
        } else {
            return; // index less than root (0); stops heapify
        }
    }


    public Question dequeue(List<Question> questions) throws NoSuchElementException {
        // if the queue is empty, throw a NoSuchElementException
        if (questions.size() == 0) {
            throw new NoSuchElementException("This Queue is empty");
        }

        Question toRemove = questions.get(0); // instantiate the course to be removed as the first element in out array

        // if there is only one element in the queue, just remove it, no need to percolateDown()
        if (questions.size() == 1) {
            questions.remove(0); // remove first element by setting first index to
        } else {
            int lastNodeIdx = questions.size() - 1;
            // put last node at root
            questions.set(0, questions.get(lastNodeIdx));
            // delete the last node (it's now at root)
            questions.remove(lastNodeIdx);
            // percolate root node down
            this.percolateDown(0, questions);
        }
        return toRemove; // return the Course that was dequeued
    }

    protected void percolateDown(int index, List<Question> questions) throws IndexOutOfBoundsException {
        if (index < 0 || index > questions.size() - 1) {
            throw new IndexOutOfBoundsException("index is out of bounds");
        } else {
            // first, we want a reference to the node we are percolating down
            Question percolatingDown = questions.get(index);
            // we want to know where the left child is (if there is one) for the node at the given index
            int indexLeftChild = 2 * index + 1; // this will be at 2n + 1

            // we want to make sure that there is a left child (i.e. it is within the bounds of our array)
            while (indexLeftChild < questions.size()) {
                // now, we need to find the max value between the current node and its children
                // we can start by assuming current node is the max value
                Question max = percolatingDown;
                // we also want to maintain the index of the max, which we'll start off at -1
                int maxIndex = -1;
                // loop through the current node's children, reassigning max if we find a greater value
                for (int i = 0; i < 2 && indexLeftChild + i < questions.size(); i++) {
                    // if the element at the current index is larger priority than our max, reset max
                    if (questions.get(indexLeftChild + i).compareTo(max) > 0) {
                        max = questions.get(indexLeftChild + i);
                        maxIndex = indexLeftChild + i; // keep track of where current max is
                    }
                }
                // if we WANT to percolate further down, then max must have been reset above (meaning
                // original max was in the wrong spot). if this is not the case, we want to stop
                if (max == percolatingDown) {
                    return; // stop, since the node we were percolating down is larger than the children
                    // don't swap duplicates
                } else {
                    // if max was reset, then max should go "above" the node we are percolating down
                    // i.e. percolatingDown and max should switch places
                    Question temp = questions.get(index);
                    questions.set(index, questions.get(maxIndex));
                    questions.set(maxIndex, temp);

                    // now, the index of the node we are percolating is where maxIndex was
                    index = maxIndex;
                    indexLeftChild = 2 * index + 1; // now, determine the index of the left child of our
                    // ONCE percolated node
                }
            }
        }
    }

}