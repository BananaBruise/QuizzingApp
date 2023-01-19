package com.app.QuizzingApp;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void user_getter_isCorrect() {
        User u = new User("Han", "Tu", "124", "htu@gmail.com", "password", true);
        String expectedfName = "Han";
        String expectedlName = "Tu";
        String expectedFullName = expectedfName + " " + expectedlName;
        String expectedEmail = "htu@gmail.com";
        String expectedUID = "124";

        assertEquals(u.getfName(),expectedfName);
        assertEquals(u.getlName(),expectedlName);
        assertEquals(u.getName(), expectedFullName);
        assertEquals(u.getEmail(), expectedEmail);
        assertEquals(u.getUID(), expectedUID);
        assertTrue(u.isQuestioner());
        assertFalse(u.getisActive());
    }

    @Test
    public void answerer_getter_isCorrect(){
        Answerer a = new Answerer("Han", "Tu", "1234", "htu@gmail.com", "password");
        String expectedfName = "Han";
        String expectedlName = "Tu";
        String expectedFullName = expectedfName + " " + expectedlName;

        assertEquals(a.getfName(),expectedfName);
        assertEquals(a.getlName(),expectedlName);
        assertEquals(a.getName(), expectedFullName);
        assertNotNull(a.getUID());
        assertFalse(a.getisActive());
        assertNull(a.getQuestionerID()); // null by default
    }

    @Test
    public void questioner_getter_isCorrect(){
        Questioner q = new Questioner("Han", "Tu", "1234", "htu@gmail.com", "password");
        String expectedfName = "Han";
        String expectedlName = "Tu";
        String expectedFullName = expectedfName + " " + expectedlName;
        int question_size = 0;

        assertEquals(q.getfName(),expectedfName);
        assertEquals(q.getlName(),expectedlName);
        assertEquals(q.getName(), expectedFullName);
        assertNotNull(q.getUID());
        assertFalse(q.getisActive());
        assertEquals(q.getQuestionList().size(), question_size);
    }

    @Test
    public void answer_getter_isCorrect(){
        Answer ansNoArg = new Answer(); // default no arg constructor
        Answer ansArg = new Answer("a custom prompt", true); // custom arg constructor

        // no arg constructor test
        assertFalse(ansNoArg.getCorrect());
        assertEquals(ansNoArg.getPrompt(), "Example prompt; please change me.");

        // constructor with arg
        assertTrue(ansArg.getCorrect());
        assertEquals(ansArg.getPrompt(), "a custom prompt");
    }

    @Test
    public void answer_setter_isCorrect(){
        Answer ansNoArg = new Answer(); // default no arg constructor

        // before modify
        assertFalse(ansNoArg.getCorrect());
        assertEquals(ansNoArg.getPrompt(), "Example prompt; please change me.");

        // modify
        ansNoArg.setCorrect(true);
        ansNoArg.setPrompt("updated prompt");

        // after modify
        assertTrue(ansNoArg.getCorrect());
        assertEquals(ansNoArg.getPrompt(), "updated prompt");
    }

    @Test
    public void question_getter_isCorrect() {
        // create a question with 4 answers
        Question q = new Question("test question", 5, 123);
        Answer a1 = new Answer("a1", true);
        Answer a2 = new Answer("a2", true);
        Answer a3 = new Answer("a3", false);
        Answer a4 = new Answer("a4", false);

        ArrayList<Answer> ans = new ArrayList<>();
        ans.add(a1);
        ans.add(a2);
        ans.add(a3);
        ans.add(a4);
        q.addAnswers(ans);

        // expected results
        String expectedQName = "test question";
        int expectedDiff = 5;
        int expectedID = 123;

        // test question attributes
        assertEquals(q.getName(), expectedQName);
        assertEquals(q.getDiff(), expectedDiff);
        assertEquals(q.getQuestionID(), expectedID);
        assertFalse(q.isCorrectlyAnsweredLastTime());
        assertEquals(q.getAnswers(), ans);
    }

    @Test
    public void question_setter_isCorrect() {
        // create a question with 4 answers
        Question q = new Question("test question", 5, 123);
        Answer a1 = new Answer("a1", true);
        Answer a2 = new Answer("a2", true);
        Answer a3 = new Answer("a3", false);
        Answer a4 = new Answer("a4", false);

        ArrayList<Answer> ans = new ArrayList<>();
        ans.add(a1);
        ans.add(a2);
        ans.add(a3);
        ans.add(a4);
        q.addAnswers(ans);

        // set/modify attributes
        q.setQuestionID(234);
        q.setDiff(1);
        q.setName("modified name");
        q.setCorrectlyAnsweredLastTime(true);
        q.addAnswers(new ArrayList<Answer>());

        // expected values
        String expectedQName = "modified name";
        int expectedDiff = 1;
        int expectedID = 234;

        // tests
        assertEquals(q.getName(), expectedQName);
        assertEquals(q.getDiff(), expectedDiff);
        assertEquals(q.getQuestionID(), expectedID);
        assertTrue(q.isCorrectlyAnsweredLastTime());
        assertEquals(q.getAnswers().size(), 0);
    }

    @Test
    public void question_heapsort_isCorrect() {
        // scenario 1: different "correctlyAnsweredLastTime"
        {

        }

        // scenario 2:
        {

        }

        // scenario 3:
        {

        }

    }
}