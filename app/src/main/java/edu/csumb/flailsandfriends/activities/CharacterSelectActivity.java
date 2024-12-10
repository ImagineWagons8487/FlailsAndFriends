package edu.csumb.flailsandfriends.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.databinding.ActivityCharacterSelectBinding;
import edu.csumb.flailsandfriends.entities.User;

public class CharacterSelectActivity extends AppCompatActivity {
    private static final String CHARACTER_SELECT_USER_ID = "edu.csumb.flailsandfriends.CHARACTER_SELECT_USER_ID";

    private static final int LOGGED_OUT = -1;

    private ActivityCharacterSelectBinding binding;

    private FlailRepo repository;

    public static final String TAG = "FLAIL_FRIENDS";

    private int loggedInUserId = -1;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCharacterSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = FlailRepo.getRepository(getApplication());

        loggedInUserId = getIntent().getIntExtra(CHARACTER_SELECT_USER_ID, LOGGED_OUT);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(LandingPageActivity.landingPageIntentFactory(getApplicationContext(), loggedInUserId));
            }
        });
    }

    public static Intent characterSelectIntentFactory(Context context, int userId){
        Intent intent = new Intent(context, CharacterSelectActivity.class);
        intent.putExtra(CHARACTER_SELECT_USER_ID, userId);
        return intent;
    }
}