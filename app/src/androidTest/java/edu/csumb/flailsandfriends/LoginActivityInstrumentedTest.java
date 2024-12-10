package edu.csumb.flailsandfriends;

import static org.junit.Assert.*;

import android.content.Context;
import android.content.Intent;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import edu.csumb.flailsandfriends.activities.LoginActivity;

public class LoginActivityInstrumentedTest {
    private LoginActivity loginActivity;

    @Test
    public void loginActivityIntentTest(){
        Context context = InstrumentationRegistry.getInstrumentation().getContext();

        Intent intent = LoginActivity.logInIntentFactory(context);

        assertNotNull(intent);
        assertEquals(LoginActivity.class.getName(), intent.getComponent().getClassName());
    }
}
