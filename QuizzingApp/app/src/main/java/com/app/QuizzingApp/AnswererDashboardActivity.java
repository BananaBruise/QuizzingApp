package com.app.QuizzingApp;

import androidx.appcompat.app.AppCompatActivity;

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

/**
 * Encodes the information used to handle the AnswererDashboardActivity app screen
 */
public class AnswererDashboardActivity extends AppCompatActivity implements CardStackListener {

    // global variables
    FirebaseHelper firebaseHelper = new FirebaseHelper();   // reference to FirebaseHelper helper class


    CheckBox correct1CB, correct2CB, correct3CB, correct4CB;    // checkbox references

    String questionerID;    // used to lookup the question set we are working with

    List<Question> cardList = new ArrayList<>();    // a List of the questions Answerer will be answering

    ActivityAnswererDashboardBinding binding;   // allows us to easily reference this activity's UI components

    CardStackLayoutManager manager; // allows our cards to be displayed in the correct order/form

    ArrayList<Question> wrong = new ArrayList<>();  // questions the Answerer has answered incorrectly

    QuestionCardAdapter adapter;    // allows us to bind our specific data to each card

    CountDownTimer cdt; // each card will contain a CountDownTimer to show the Answerer their time taken

    TextView cardTimerTV; // timer TV on each card that we'll be updating

    long millisElapsedForQuestion;  // how much time the Answerer has taken for a single question

    // timer variables
    long totalSeconds = 180;    // 3 min timer
    long warningSeconds = 10;   // after 10 seconds remaining timer will flash red
    long intervalSeconds = 1;   // counts down every second

    // formatting tool
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("mm:ss");


    /**
     * When the screen is loaded, the data that needs to be displayed will be put into an Adapter,
     * which is then displayed on the screen as a card stack. The card stack is managed by
     * CardStackLayoutManager
     * @param savedInstanceState may be used to restore activity to a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // grab layout (binding) of this activity
        binding = ActivityAnswererDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // create manager object for cardstack (first arg = context, second arg = listener interface)
        manager = new CardStackLayoutManager(this, this);
        manager.setStackFrom(StackFrom.Top);    // appearance of stack
        manager.setVisibleCount(3); // 3 cards visible at a time
        manager.setDirections(Direction.HORIZONTAL);    // can scroll horizontally
        manager.setCanScrollVertical(false);  // cannot scroll vertically

        // populate stack of questions for answerer
        String uid = firebaseHelper.getmAuth().getCurrentUser().getUid();
        firebaseHelper.readGenericUser(uid, Answerer.class, new FirebaseHelper.FirestoreUserCallback() {
            @Override
            public void onCallbackReadUser(User u) {
                Answerer answerer = (Answerer) u;
                questionerID = answerer.getQuestionerID();
                // here we are using questionerID field to lookup questions posted by this Answerer's Questioner
                firebaseHelper.readQuestions(questionerID, new FirebaseHelper.FirestoreQuestionCallback() {
                    @Override
                    public void onCallbackReadQuestions(ArrayList<Question> questionList) {
                        cardList = heapSort(questionList);  // heap sort each time we grab questions
                                                            // as question parameters can change each
                                                            // time Answerer takes quiz

                        // no questions yet
                        if (cardList.isEmpty()) {
                           Toast.makeText(getApplicationContext(), "You don't have any questions yet!", Toast.LENGTH_SHORT).show();
                        } else {
                            // put the card list in the view
                            adapter = new QuestionCardAdapter(cardList);
                            binding.cardStack.setLayoutManager(manager);
                            binding.cardStack.setAdapter(adapter);
                        }


                    }
                });

            }
        });

    }


    /**
     * Signs a user out of the app
     * @param v the view corresponding to the current screen
     */
    public void signOut(View v) {
         new Navigation().signOut(AnswererDashboardActivity.this);
    }

    // THE FOLLOWING 4 METHODS ARE UNUSED CALLBACKS
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

    /**
     * When a card appears, we start a timer. When that timer finishes, the card is automatically swiped
     * off the screen
     * @param view view object corresponding to current card
     * @param position where we are in the stack of cards (as an index)
     */
    @Override
    public void onCardAppeared(View view, int position) {
        // get reference to timerTV
        cardTimerTV = view.findViewById(R.id.timerTV);

        // instantiate Animation object with "blink" animation (see blink.xml)
        Animation a = AnimationUtils.loadAnimation(this, R.anim.blink);

        // instantiate CountDownTimer with time limit and counting interval (every second)
        cdt = new CountDownTimer(totalSeconds * 1000, intervalSeconds * 1000) {

            // callback; called every time timer "ticks" (i.e. a second has passed)
            public void onTick(long millisUntilFinished) {
                // if there are less than 10 seconds remaining, flash timer in red color
                if (millisUntilFinished <= ((warningSeconds + 1) * 1000)) {
                    cardTimerTV.setTextColor(Color.RED);
                    cardTimerTV.startAnimation(a);
                } else {
                    // otherwise timer will stay green
                    cardTimerTV.setTextColor(getColor(R.color.green));
                }

                // update the timer appropriately each time it "ticks"
                cardTimerTV.setText(mSimpleDateFormat.format(millisUntilFinished));


                // also update our tracker of time taken for this question
                millisElapsedForQuestion = (totalSeconds * 1000) - millisUntilFinished;
            }

            // callback; called when timer ends
            public void onFinish() {
                // update timer appropriately
                cardTimerTV.setText(mSimpleDateFormat.format(0));
                binding.cardStack.swipe();  // swipe automatically (Answerer ran out of time)
                Toast.makeText(getApplicationContext(), "You ran out of time!", Toast.LENGTH_SHORT).show();
            }
        };

        cdt.start();    // start the timer when this card appears

    }

    /**
     * When a card disappears, we must update our Firestore database appropriately
     * @param view view corresponding to current card
     * @param position where we are in our card stack
     */
    @Override
    public void onCardDisappeared(View view, int position) {
        // get references to UI elements
        correct1CB = view.findViewById(R.id.cor1CB);
        correct2CB = view.findViewById(R.id.cor2CB);
        correct3CB = view.findViewById(R.id.cor3CB);
        correct4CB = view.findViewById(R.id.cor4CB);

        // user entered answers (true = checkbox checked; false = not checked)
        List<Boolean> entered = new ArrayList<>();
        entered.add(correct1CB.isChecked());
        entered.add(correct2CB.isChecked());
        entered.add(correct3CB.isChecked());
        entered.add(correct4CB.isChecked());

        // "correct" answers
        List<Answer> answerList = cardList.get(position).getAnswers();

        // question doc ID (corresponding to the Question the user is currently on)
        String questionDocID = Integer.toString(cardList.get(position).getQuestionID());

        Boolean isCorrect = true;   // assume Question is correct unless we detect otherwise

        // for each answer the user has selected
        for (int i = 0; i < entered.size(); i++) {
            // if it doesn't match with what should be correct
            if (entered.get(i) != answerList.get(i).getCorrect()) {
                isCorrect = false;  // this Question is wrong
                wrong.add(cardList.get(position)); // keep track of this wrong Question
                Log.i("onCardDisappeared", "Question was incorrect!");
                break;  // no need to check other answers if we reach one incorrect one
            }
        }

        // update time taken for card
        firebaseHelper.updateQuestionField(questionerID, questionDocID, "millisElapsedToAnswer", (Long) millisElapsedForQuestion, new FirebaseHelper.FirestoreQuestionCallback() {
            @Override
            public void onCallbackUpdateQuestionField() {
                Log.i("onCardDisappeared", "updated time stamp!");
            }
        });

        // update question status (if Question was correct or not)
        firebaseHelper.updateQuestionField(questionerID, questionDocID, "correctlyAnsweredLastTime", isCorrect, new FirebaseHelper.FirestoreQuestionCallback() {
            @Override
            public void onCallbackUpdateQuestionField() {
                // on callback, if we reach the end of our question cards
                if (position == cardList.size() - 1) {
                    // display appropriate toast and take user to PostQuizActivity (quiz results)
                    Toast.makeText(getApplicationContext(), "You finished the quiz!", Toast.LENGTH_SHORT).show();
                    takeToPostQuizActivity(wrong);
                }
            }
        });

        cdt.cancel();   // stop timer for this card since user has finished it

    }

    /**
     * Takes us to PostQuizActivity
     * @param wrongQuestions questions the Answerer got wrong on this page
     */
    public void takeToPostQuizActivity(ArrayList<Question> wrongQuestions) {
        // we want to take the user to PostQuizActivity
        Intent i = new Intent(getApplicationContext(), PostQuizActivity.class);

        // use a Bundle to send ArrayList of custom type to new screen
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("WRONGS", wrongQuestions);

        // put Bundle in Intent and send info to PostQuizActivity
        i.putExtra("INCORRECT_QUESTIONS", bundle);
        startActivity(i);

    }

    /**
     * Sorts a given array of questions by, in decreasing order of importance:
     * Order of ranking:
     *      * 1. if the user missed the question last time (higher priority)
     *      * 2. time the user spent answering the question (higher time = higher priority)
     *      * 3. difficulty of question (higher difficulty = higher priority)
     * @param questions Question ArrayList we are heap sorting
     * @return a sorted version of questions, in order from highest priority to lowest priority (max-heap)
     */
    protected static ArrayList<Question> heapSort(List<Question> questions) {
        // heapify (so that when we dequeue we will always get the max priority element)
        heapify(questions);

        // sort
        int arraySize = questions.size();

        ArrayList<Question> result = new ArrayList<>(); // we are sorting out-of-place

        // since we've heapified, all we need to do is dequeue each element
        for (int i = 0; i < arraySize; i++) {
            result.add(dequeue(questions));
        }

        return result;  // sorted array
    }

    /**
     * Heapifies a given question ArrayList so it can be heap sorted
     * @param questions array we are heapifying
     */
    public static void heapify(List<Question> questions) {
        // empty or 1 item heap is already a heap
        if (questions.isEmpty() || questions.size() == 1)
            return;

        // heapify starting from last internal node
        heapifyHelper((questions.size() - 2) / 2, questions);
    }

    /**
     * Recursive helper method to heapify a given array of questions
     * @param index index we are going to start percolateDown() at
     * @param questions array we are heapifying
     */
    protected static void heapifyHelper(int index, List<Question> questions) {
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

    /**
     * Removes and returns the next highest priority Question in the given array list
     * @param questions the given array list we are checking for the highest priority element
     * @return the next highest priority Question
     * @throws NoSuchElementException if the list is empty
     */
    public static Question dequeue(List<Question> questions) throws NoSuchElementException {
        // if the list is empty, throw a NoSuchElementException
        if (questions.size() == 0) {
            throw new NoSuchElementException("This list is empty");
        }

        Question toRemove = questions.get(0); // instantiate the Question to be removed as the first element in our array

        // if there is only one element in the queue, just remove it, no need to percolateDown()
        if (questions.size() == 1) {
            questions.remove(0); // remove first element
        } else {
            int lastNodeIdx = questions.size() - 1;
            // put last node at root
            questions.set(0, questions.get(lastNodeIdx));
            // delete the last node (it's now at root)
            questions.remove(lastNodeIdx);
            // percolate root node down
            percolateDown(0, questions);
        }
        return toRemove; // return the Question that was dequeued
    }

    /**
     * Percolates down a node starting at a given index
     * @param index the node we are percolating down from
     * @param questions the list we are percolating with respect to
     * @throws IndexOutOfBoundsException if given index is OOB for list
     */
    protected static void percolateDown(int index, List<Question> questions) throws IndexOutOfBoundsException {
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