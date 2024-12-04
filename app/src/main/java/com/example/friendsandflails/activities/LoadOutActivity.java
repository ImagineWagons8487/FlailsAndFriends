package com.example.friendsandflails.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.friendsandflails.database.FlailRepo;
import com.example.friendsandflails.databinding.ActivityCombatBinding;
import com.example.friendsandflails.databinding.ActivityLoadoutBinding;
import com.example.friendsandflails.entities.User;

public class LoadOutActivity extends AppCompatActivity {

    private ActivityLoadoutBinding binding;

    private FlailRepo repository;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoadoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = FlailRepo.getRepository(getApplication());

//        binding.lightButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                verifyUser();
//            }
//        });
    }

    static Intent loadOutActivityIntentFactory(Context context) {
        return new Intent(context, LoadOutActivity.class);
    }
}