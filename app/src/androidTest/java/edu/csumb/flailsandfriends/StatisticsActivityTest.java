package edu.csumb.flailsandfriends;

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
import edu.csumb.flailsandfriends.activities.StatisticsActivity;
import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.entities.BattleRecord;

@RunWith(AndroidJUnit4.class)
public class StatisticsActivityTest {

    @Test
    public void testSummaryUpdates() {
        // Launch activity with the correct class
        ActivityScenario<StatisticsActivity> scenario = ActivityScenario.launch(StatisticsActivity.class);

        scenario.onActivity(activity -> {
            // Access the repository
            FlailRepo repository = FlailRepo.getRepository(activity.getApplication());

            // Insert mock data
            BattleRecord mockRecord = new BattleRecord(1, "Mock Title", "Win");
            repository.insertBattleRecord(mockRecord);

            // Trigger UI update (RecyclerView adapter should auto-update if LiveData is used correctly)
            activity.statisticsRecyclerView.getAdapter().notifyDataSetChanged();

            // Assert the total games are updated correctly
            onView(withId(R.id.totalGamesTextView)).check(matches(withText("Total Games: 1")));
        });
    }
}


