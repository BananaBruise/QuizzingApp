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
    public void user_name_age_isCorrect() {
        User u = new User("Han", 34, new ArrayList<String>());
        String expectedName = "Han";
        int expectedAge = 34;

        assertEquals(u.getName(),expectedName);
        assertEquals(u.getAge(),expectedAge);
    }
}