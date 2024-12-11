package edu.csumb.flailsandfriends;

import static org.junit.Assert.assertEquals;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.csumb.flailsandfriends.entities.BattleRecord;

public class StatisticsAdapterTest {

    @Test
    public void testAdapterItemCount() {
        // Arrange
        List<BattleRecord> records = new ArrayList<>();
        records.add(new BattleRecord(1, "Test Title 1", "Win"));
        records.add(new BattleRecord(2, "Test Title 2", "Lose"));
        StatisticsAdapter adapter = new StatisticsAdapter(records);

        // Act & Assert
        assertEquals("Adapter item count should match records size", 2, adapter.getItemCount());
    }

    @Test
    public void testAdapterBindView() {
        // Arrange
        List<BattleRecord> records = new ArrayList<>();
        records.add(new BattleRecord(1, "Test Title 1", "Win"));
        StatisticsAdapter adapter = new StatisticsAdapter(records);

        // Create a mock parent ViewGroup
        ViewGroup parent = new ViewGroup(ApplicationProvider.getApplicationContext()) {
            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {
            }
        };

        // Act
        StatisticsAdapter.StatisticsViewHolder viewHolder = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(viewHolder, 0);

        // Assert
        assertEquals("Test Title 1", viewHolder.titleTextView.getText().toString());
        assertEquals("Win", viewHolder.recordTextView.getText().toString());
    }
}

