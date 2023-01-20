package com.app.QuizzingApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Manages the creation of a Question object
 */
public class QuestionMakerActivity extends AppCompatActivity {
    // references to UI elements
    EditText qName;
    RatingBar difficulty;
    CheckBox correct1, correct2, correct3, correct4;
    EditText answerText1, answerText2, answerText3, answerText4;
    TextView qLength, answerLength1, answerLength2, answerLength3, answerLength4;

    // max lengths for answer prompts and question name (characters)
    final int MAX_ANSWER_TEXT_LENGTH = 24;
    final int MAX_QUESTION_TEXT_LENGTH = 50;

    FirebaseHelper firebaseHelper = new FirebaseHelper();   // reference to helper class

    /**
     * Instantiates UI elements as well as dynamic character counter for each edit text
     * @param savedInstanceState may be used to restore activity to a previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_maker);

        // instantiate all UI elements
        qName = findViewById(R.id.questionNameET);
        qLength = findViewById(R.id.questionMakerQuestionLengthTV);
        difficulty = findViewById(R.id.difficultyBar);
        correct1 = findViewById(R.id.questionMakerCorrect1CB);
        answerText1 = findViewById(R.id.questionMakerAnswer1ET);
        answerLength1 = findViewById(R.id.questionMakerAnswerLengthTV1);
        correct2 = findViewById(R.id.questionMakerCorrect2CB);
        answerText2 = findViewById(R.id.questionMakerAnswer2ET);
        answerLength2 = findViewById(R.id.questionMakerAnswerLengthTV2);
        correct3 = findViewById(R.id.questionMakerCorrect3CB);
        answerText3 = findViewById(R.id.questionMakerAnswer3ET);
        answerLength3 = findViewById(R.id.questionMakerAnswerLengthTV3);
        correct4 = findViewById(R.id.questionMakerCorrect4CB);
        answerText4 = findViewById(R.id.questionMakerAnswer4ET);
        answerLength4 = findViewById(R.id.questionMakerAnswerLengthTV4);

        // set max lengths
        setMaxLength(qName, MAX_QUESTION_TEXT_LENGTH);
        setMaxLength(answerText1, MAX_ANSWER_TEXT_LENGTH);
        setMaxLength(answerText2, MAX_ANSWER_TEXT_LENGTH);
        setMaxLength(answerText3, MAX_ANSWER_TEXT_LENGTH);
        setMaxLength(answerText4, MAX_ANSWER_TEXT_LENGTH);

        // interactive length checker
        qName.addTextChangedListener(new TextWatcher() {

            // callback; called when text in edit text has been altered
            public void afterTextChanged(Editable s) {
                int length = s.length();    // length of new String
                String text = length + "/" + MAX_QUESTION_TEXT_LENGTH;

                // if Editable is blank, no character counter needed
                if (length == 0) {
                    qLength.setVisibility(View.INVISIBLE);
                } else if (length == MAX_QUESTION_TEXT_LENGTH) {
                    // if Editable is full, display count in red
                    qLength.setTextColor(Color.RED);
                    qLength.setText(text);
                    qLength.setVisibility(View.VISIBLE);
                }
                // length somewhere between 0 and max
                else {
                    // otherwise, counter will be green
                    qLength.setTextColor(getColor(R.color.dkgreen));
                    qLength.setText(text);
                    qLength.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // unused
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // unused
            }
        });

        answerText1.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                int length = s.length();
                String text = length + "/" + MAX_ANSWER_TEXT_LENGTH;

                if (length == 0) {
                    answerLength1.setVisibility(View.INVISIBLE);
                } else if (length == MAX_ANSWER_TEXT_LENGTH) {
                    answerLength1.setTextColor(Color.RED);
                    answerLength1.setText(text);
                    answerLength1.setVisibility(View.VISIBLE);
                }
                // length somewhere between 0 and max
                else {
                    answerLength1.setTextColor(getColor(R.color.dkgreen));
                    answerLength1.setText(text);
                    answerLength1.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // unused
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // unused
            }
        });

        answerText2.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                int length = s.length();
                String text = length + "/" + MAX_ANSWER_TEXT_LENGTH;

                if (length == 0) {
                    answerLength2.setVisibility(View.INVISIBLE);
                } else if (length == MAX_ANSWER_TEXT_LENGTH) {
                    answerLength2.setTextColor(Color.RED);
                    answerLength2.setText(text);
                    answerLength2.setVisibility(View.VISIBLE);
                }
                // length somewhere between 0 and max
                else {
                    answerLength2.setTextColor(getColor(R.color.dkgreen));
                    answerLength2.setText(text);
                    answerLength2.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // unused
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // unused
            }
        });

        answerText3.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                int length = s.length();
                String text = length + "/" + MAX_ANSWER_TEXT_LENGTH;

                if (length == 0) {
                    answerLength3.setVisibility(View.INVISIBLE);
                } else if (length == MAX_ANSWER_TEXT_LENGTH) {
                    answerLength3.setTextColor(Color.RED);
                    answerLength3.setText(text);
                    answerLength3.setVisibility(View.VISIBLE);
                }
                // length somewhere between 0 and max
                else {
                    answerLength3.setTextColor(getColor(R.color.dkgreen));
                    answerLength3.setText(text);
                    answerLength3.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // unused
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // unused
            }
        });

        answerText4.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                int length = s.length();
                String text = length + "/" + MAX_ANSWER_TEXT_LENGTH;

                if (length == 0) {
                    answerLength4.setVisibility(View.INVISIBLE);
                } else if (length == MAX_ANSWER_TEXT_LENGTH) {
                    answerLength4.setTextColor(Color.RED);
                    answerLength4.setText(text);
                    answerLength4.setVisibility(View.VISIBLE);
                }
                // length somewhere between 0 and max
                else {
                    answerLength4.setTextColor(getColor(R.color.dkgreen));
                    answerLength4.setText(text);
                    answerLength4.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // unused
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // unused
            }
        });


    }

    /**
     * Sets the max length attribute of the given TextView
     * @param tv the given TextView we are setting the max length attribute of
     * @param textLength the max length
     * @param <T> generic; must be a subclass of TextView
     */
    protected <T extends TextView> void setMaxLength(T tv, int textLength) {
        tv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(textLength)});
    }

    /**
     * Adds a Question object with specified parameters to our database (makes use of FirebaseHelper
     * methods)
     * @param v the view corresponding to the current screen
     */
    public void submitQuestion(View v) {
        // question attributes
        String name = qName.getText().toString();
        int diff = (int) difficulty.getRating();

        // answer attributes
        boolean c1 = correct1.isChecked();
        String t1 = answerText1.getText().toString();
        boolean c2 = correct2.isChecked();
        String t2 = answerText2.getText().toString();
        boolean c3 = correct3.isChecked();
        String t3 = answerText3.getText().toString();
        boolean c4 = correct4.isChecked();
        String t4 = answerText4.getText().toString();

        // check blank
        if (t1.isEmpty() || t2.isEmpty() || t3.isEmpty() || t4.isEmpty() || name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill in all fields!", Toast.LENGTH_SHORT).show();
        } else {
            int qid = (int) (Math.random() * 1000000);  // create random question ID for each question
            // create our answers and question
            // question
            Question q = new Question(name, diff, qid);
            // answers
            Answer a1 = new Answer(t1, c1);
            Answer a2 = new Answer(t2, c2);
            Answer a3 = new Answer(t3, c3);
            Answer a4 = new Answer(t4, c4);
            // add answers to question
            q.addAnswers(new ArrayList<Answer>(Arrays.asList(a1, a2, a3, a4)));

            // submit as a collection to current ID document
            String uid = firebaseHelper.getmAuth().getCurrentUser().getUid();

            firebaseHelper.writeQuestion(uid, Integer.toString(q.getQuestionID()), q, new FirebaseHelper.FirestoreQuestionCallback() {
                @Override
                public void onCallbackWriteQuestion() {
                    Log.i("QuestionMakerActivity", "question added");
                    Toast.makeText(getApplicationContext(), "Question added successfully!",
                            Toast.LENGTH_SHORT).show();
                    // take back to dashboard
                    startActivity(new Intent(getApplicationContext(), QuestionerDashboardActivity.class));
                }
            });
        }
    }


}