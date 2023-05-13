package com.app.QuizzingApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

/**
 * Manages the tasks of QuestionerDashboardActivity screen
 */
public class QuestionerDashboardActivity extends AppCompatActivity {

    FirebaseHelper firebaseHelper = new FirebaseHelper();   // reference to helper class
    EditText searchET;  // reference to search box
    ArrayList<Question> questionsList = new ArrayList<>();  // questions this Questioner has posted

    /**
     * Reads all questions posted by current Questioner and displays them in a list view
     * @param savedInstanceState may be used to restore activity to a previous state
     */
    @Override
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questioner_dashboard);

        searchET = findViewById(R.id.searchET); // instantiate reference to search box

        // uid of current user
        String uid = firebaseHelper.getmAuth().getCurrentUser().getUid();

        // read the questions corresponding to this user
        firebaseHelper.readQuestions(uid, new FirebaseHelper.FirestoreQuestionCallback() {
            @Override
            public void onCallbackReadQuestions(ArrayList<Question> questionList) {
                questionsList = questionList;
                displayQuestions(questionsList);    // display these questions in the LV
            }
        });

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    search(""); // reset search when it becomes empty
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });
    }

    /**
     * Displays the given list of questions in a list view
     * @param questionsList the list of questions we are displaying
     */
    public void displayQuestions(ArrayList<Question> questionsList) {
        // check if there are no questions
        if (questionsList.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No questions!", Toast.LENGTH_LONG).show();
        }

        // sort questions in difficulty order
        Collections.sort(questionsList, new Comparator<Question>() {
            @Override
            public int compare(Question question1, Question question2) {
                if (question1.getDiff() < question2.getDiff()) {
                    return -1;
                } else if (question1.getDiff() > question2.getDiff()) {
                    return +1;
                } else {
                    return 0;
                }
            }
        });
        // display questionsList in this list view
        ArrayAdapter<Question> listAdapter = new QuestionListAdapter(
                getApplicationContext(), questionsList);


        ListView listView = (ListView) findViewById(R.id.allQuestionsLV);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // if the user clicks on an element in the LV, they are taken to ViewQuestionActivity
                Intent intent = new Intent(getApplicationContext(), ViewQuestionActivity.class);

                // send this Question's data to next screen
                intent.putExtra("ITEM", questionsList.get(i));
                startActivity(intent);
            }
        });
    }

    /**
     * Allows a user to create a question
     * @param v the view object corresponding to the current screen
     */
    public void takeToQuestionMaker(View v) {
        startActivity(new Intent(getApplicationContext(), QuestionMakerActivity.class));
    }

    /**
     * Signs out the current user
     * @param v the view object corresponding to the current screen
     */
    public void signOut(View v) {
        new Navigation().signOut(QuestionerDashboardActivity.this);
    }

    public void searchHelper(View v) {
        String key = searchET.getText().toString();

        // if there is a key, we must search, so call search()
        if (!key.isEmpty()) {
            search(key);
        } else {
            displayQuestions(questionsList);    // otherwise "reset" search
        }
    }

    /**
     * Allows the question list to be searched by a given questionName
     * @param questionName the question name we are searching for
     */
    public void search(String questionName) {

        ArrayList<Question> searched = new ArrayList<Question>();

        // find all Questions that match questionName query
        for (int i = 0; i < questionsList.size(); i++) {
            if (questionsList.get(i).getName().toLowerCase().contains(questionName.trim().toLowerCase())) {
                searched.add(questionsList.get(i));
            }
        }

        displayQuestions(searched); // display resulting "searched" AL
    }
}

