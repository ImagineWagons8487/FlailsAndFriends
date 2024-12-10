package edu.csumb.flailsandfriends.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.csumb.flailsandfriends.R;
import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.databinding.ActivityPostMatchBinding;

public class PostMatchActivity extends AppCompatActivity {
    private static final String CHARACTER_SELECT_USER_ID = "edu.csumb.flailsandfriends.CHARACTER_SELECT_USER_ID";

    private static final int LOGGED_OUT = -1;

    private ActivityPostMatchBinding binding;
    private FlailRepo repository;
    private static int loggedInUserId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPostMatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = FlailRepo.getRepository(getApplication());

        loggedInUserId = getIntent().getIntExtra(CHARACTER_SELECT_USER_ID, LOGGED_OUT);

        setContentView(R.layout.activity_post_match);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.rematch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(CombatActivity.combatActivityIntentFactory(getApplicationContext()));
            }
        });

        binding.ReturnToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(LandingPageActivity.landingPageIntentFactory(getApplicationContext(), loggedInUserId));
            }
        });
    }



    public static Intent postMatchIntentFactory(Context context) {
        return new Intent(context, PostMatchActivity.class);
    }
}