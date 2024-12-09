package edu.csumb.flailsandfriends.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.csumb.flailsandfriends.R;
import edu.csumb.flailsandfriends.database.FlailRepo;

public class GameOverActivity extends AppCompatActivity {

    private FlailRepo repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // Initialize the repository for database interaction
        repository = FlailRepo.getRepository(getApplication());

        // Find buttons from the layout
        Button retryButton = findViewById(R.id.retryButton);
        Button quitButton = findViewById(R.id.quitButton);

        // Set up button listeners
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restart the game (navigate back to CombatActivity)
                Intent intent = new Intent(GameOverActivity.this, CombatActivity.class);
                startActivity(intent);
                finish();
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quit the game
                finish();
            }
        });

        // Example database interaction: Log game over event
        logGameResult();
    }

    private void logGameResult() {
        // Example: Log the game over result in the database
        repository.insertBattleRecord(new edu.csumb.flailsandfriends.entities.BattleRecord(
                1, "Game Over", "Player lost"
        ));
    }

    // Factory method to create an Intent for this activity
    public static Intent gameOverIntentFactory(Context context) {
        return new Intent(context, GameOverActivity.class);
    }
}

