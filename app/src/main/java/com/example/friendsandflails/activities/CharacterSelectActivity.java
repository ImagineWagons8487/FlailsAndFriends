package com.example.friendsandflails.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.friendsandflails.database.FlailRepo;
import com.example.friendsandflails.databinding.ActivityCharacterSelectBinding;
import com.example.friendsandflails.entities.User;

public class CharacterSelectActivity extends AppCompatActivity {
    private static final String CHARACTER_SELECT_USER_ID = "com.example.friendsandflails.CHARACTER_SELECT_USER_ID";

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

    static Intent characterSelectIntentFactory(Context context, int userId){
        Intent intent = new Intent(context, CharacterSelectActivity.class);
        intent.putExtra(CHARACTER_SELECT_USER_ID, userId);
        return intent;
    }
}