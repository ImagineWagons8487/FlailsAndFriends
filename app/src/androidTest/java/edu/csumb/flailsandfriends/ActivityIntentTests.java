package edu.csumb.flailsandfriends;

import static org.junit.Assert.*;

import android.content.Context;
import android.content.Intent;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import edu.csumb.flailsandfriends.activities.CharacterSelectActivity;
import edu.csumb.flailsandfriends.activities.CombatActivity;
import edu.csumb.flailsandfriends.activities.CreateAccountActivity;
import edu.csumb.flailsandfriends.activities.LandingPageActivity;
import edu.csumb.flailsandfriends.activities.LoadOutActivity;
import edu.csumb.flailsandfriends.activities.LoginActivity;
import edu.csumb.flailsandfriends.activities.MainActivity;
import edu.csumb.flailsandfriends.activities.PostMatchActivity;
import edu.csumb.flailsandfriends.activities.PostMatchActivityLose;

public class ActivityIntentTests {

    private Context context = InstrumentationRegistry.getInstrumentation().getContext();
    int id = 420;

    @Test
    public void loginActivityIntentTest(){
        Intent intent = LoginActivity.logInIntentFactory(context);

        assertNotNull(intent);
        assertEquals(LoginActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void mainActivityIntentTest(){
        Intent intent = MainActivity.mainActivityIntentFactory(context);

        assertNotNull(intent);
        assertEquals(MainActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void loadOutActivityIntentTest(){
        Intent intent = LoadOutActivity.loadOutActivityIntentFactory(context, id);

        assertNotNull(intent);
        assertEquals(LoadOutActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void landingPageActivityIntentTest(){
        Intent intent = LandingPageActivity.landingPageIntentFactory(context, id);

        assertNotNull(intent);
        assertEquals(LandingPageActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void createAccountActivityIntentTest(){
        Intent intent = CreateAccountActivity.createAccountIntentFactory(context);

        assertNotNull(intent);
        assertEquals(CreateAccountActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void combatActivityIntentTest(){
        Intent intent = CombatActivity.combatActivityIntentFactory(context);

        assertNotNull(intent);
        assertEquals(CombatActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void characterSelectActivityIntentTest(){
        Intent intent = CharacterSelectActivity.characterSelectIntentFactory(context, id);

        assertNotNull(intent);
        assertEquals(CharacterSelectActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void postMatchActivityLoseIntentTest(){
        Intent intent = PostMatchActivityLose.postMatchLoseIntentFactory(context);

        assertNotNull(intent);
        assertEquals(PostMatchActivityLose.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void postMatchActivityIntentTest(){
        Intent intent = PostMatchActivity.postMatchIntentFactory(context);

        assertNotNull(intent);
        assertEquals(PostMatchActivity.class.getName(), intent.getComponent().getClassName());
    }
}
