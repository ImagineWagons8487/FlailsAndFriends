package edu.csumb.flailsandfriends.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.csumb.flailsandfriends.R;
import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.databinding.ActivityGameOverBinding;
import edu.csumb.flailsandfriends.databinding.ActivityLandingPageBinding;

public class GameOverActivity extends AppCompatActivity {

    private FlailRepo repository;

    private ActivityGameOverBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameOverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the repository for database interaction
        repository = FlailRepo.getRepository(getApplication());


        // Set up button listeners
        binding.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restart the game (navigate back to CombatActivity)
                Intent intent = new Intent(GameOverActivity.this, GameLoopActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quit the game
                startActivity(LandingPageActivity.landingPageIntentFactory(getApplicationContext(), 4));
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
    public static Intent gameOverIntentFactory(Context context, String battleLog) {
        Intent intent = new Intent(context, GameOverActivity.class);
        intent.putExtra("battleLog", battleLog);
        return intent;
    }
}

