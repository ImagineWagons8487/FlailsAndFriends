package com.example.friendsandflails.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.friendsandflails.R;
import com.example.friendsandflails.database.FlailRepo;
import com.example.friendsandflails.databinding.ActivityCombatBinding;

import com.example.friendsandflails.entities.User;

public class CombatActivity extends AppCompatActivity {

    private ActivityCombatBinding binding;

    private FlailRepo repository;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCombatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = FlailRepo.getRepository(getApplication());

//        binding.lightButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                verifyUser();
//            }
//        });
    }

    static Intent combatActivityIntentFactory(Context context) {
        return new Intent(context, CombatActivity.class);
    }
}