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
}