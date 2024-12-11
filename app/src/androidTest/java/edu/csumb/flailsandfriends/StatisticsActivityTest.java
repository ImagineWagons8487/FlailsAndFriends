package edu.csumb.flailsandfriends.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.csumb.flailsandfriends.R;
import edu.csumb.flailsandfriends.StatisticsAdapter;
import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.entities.BattleRecord;

@RunWith(AndroidJUnit4.class)
public class StatisticsActivityTest {

    @Test
    public void testSummaryUpdates() {
        // Launch activity
        ActivityScenario<StatisticsActivity> scenario = ActivityScenario.launch(new Intent());

        scenario.onActivity(activity -> {
            // Mock repository data
            FlailRepo repository = FlailRepo.getRepository(activity.getApplication());
            repository.insertBattleRecord(new BattleRecord(1, "Mock Title", "Win"));

            // Force adapter to update
            StatisticsAdapter adapter = new StatisticsAdapter(repository.getBattleRecords().getValue());
            activity.statisticsRecyclerView.setAdapter(adapter);

            // Assert total games are updated
            onView(withId(R.id.totalGamesTextView)).check(matches(withText("Total Games: 1")));
        });
    }
}

