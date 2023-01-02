package com.app.QuizzingApp;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

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

        assertEquals(u.getfName(),expectedfName);
        assertEquals(u.getlName(),expectedlName);
        assertEquals(u.getName(), expectedFullName);
        assertNotNull(u.getUID());
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
        assertNull(a.getQuestionID());
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
}