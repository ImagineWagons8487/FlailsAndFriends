package edu.csumb.flailsandfriends.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.csumb.flailsandfriends.R;
import edu.csumb.flailsandfriends.StatisticsAdapter;
import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.entities.BattleRecord;

public class StatisticsActivity extends AppCompatActivity {

    private RecyclerView statisticsRecyclerView;
    private TextView totalGamesTextView;
    private FlailRepo repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Initialize views
        statisticsRecyclerView = findViewById(R.id.statisticsRecyclerView);
        totalGamesTextView = findViewById(R.id.totalGamesTextView);

        // Set up RecyclerView
        statisticsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize repository
        repository = FlailRepo.getRepository(getApplication());

        // Observe battle records and populate RecyclerView
        repository.getBattleRecords().observe(this, new Observer<List<BattleRecord>>() {
            @Override
            public void onChanged(List<BattleRecord> battleRecords) {
                // Set up the adapter
                StatisticsAdapter adapter = new StatisticsAdapter(battleRecords);
                statisticsRecyclerView.setAdapter(adapter);

                // Update summary data
                updateSummary(battleRecords.size());
            }
        });
    }

    // Updated updateSummary method
    private void updateSummary(int totalGames) {
        totalGamesTextView.setText(totalGames > 0 ? "Total Games: " + totalGames : "No games played yet.");
    }
}
