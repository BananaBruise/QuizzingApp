package com.app.QuizzingApp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MockitoUnitTest {
    private final String TEST_APPNAME = "sample_app";

    @Mock
    private Context mockContext;

    @Before
    public void initMocks(){
        mockContext = mock(Context.class);
        when(mockContext.getString(R.string.app_name)).thenReturn("sample_app");
    }

    @Test
    public void context_getString_isCorrect() {
        String result = mockContext.getString(R.string.app_name);
        assertEquals(result,TEST_APPNAME);
    }


}
