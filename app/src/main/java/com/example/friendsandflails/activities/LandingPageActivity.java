package com.example.friendsandflails.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.friendsandflails.R;
import com.example.friendsandflails.database.FlailRepo;
import com.example.friendsandflails.databinding.ActivityLandingPageBinding;
import com.example.friendsandflails.entities.User;

public class LandingPageActivity extends AppCompatActivity {
    private static final String LANDING_PAGE_USER_ID = "com.example.friendsandflails.LANDING_PAGE_USER_ID";

    static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.friendsandflails.SAVED_INSTANCE_STATE_USERID_KEY";

    private static final int LOGGED_OUT = -1;

    private ActivityLandingPageBinding binding;

    private FlailRepo repository;

    public static final String TAG = "FLAIL_FRIENDS";

    private int loggedInUserId = -1;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = FlailRepo.getRepository(getApplication());

        logInUser(savedInstanceState);

        if(loggedInUserId == LOGGED_OUT){
            Intent intent = LoginActivity.logInIntentFactory(getApplicationContext());
            startActivity(intent);
        }
        updateSharedPreference();

        binding.fightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(CombatActivity.combatActivityIntentFactory(getApplicationContext()));
            }
        });

        binding.loadoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(LoadOutActivity.loadOutActivityIntentFactory(getApplicationContext()));
            }
        });
    }

    private void logInUser(Bundle savedInstanceState){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userid_key), LOGGED_OUT);

        if(loggedInUserId == LOGGED_OUT & savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)){
            loggedInUserId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT){
            loggedInUserId = getIntent().getIntExtra(LANDING_PAGE_USER_ID, LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT){
            return;
        }

        LiveData<User> userObserver = repository.getUserByUserId(loggedInUserId);
        userObserver.observe(this,user -> {
            this.user = user;
            if(user != null){
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserId);
        updateSharedPreference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem item  = menu.findItem(R.id.logoutMenuId);
        item.setVisible(true);
        if(user == null){
            return false;
        }
        item.setTitle(user.getUsername());
        item.setOnMenuItemClickListener(menuItem -> {
            showLogoutDialog();
            return false;
        });
        return true;
    }

    private void showLogoutDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LandingPageActivity.this);
        final AlertDialog alertDialog = alertBuilder.create();

        alertBuilder.setMessage("Logout");

        alertBuilder.setPositiveButton("Logout", (dialogInterface, i) -> logout());

        alertBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> alertDialog.dismiss());

        alertBuilder.create().show();
    }

    private void logout(){
        loggedInUserId = LOGGED_OUT;
        updateSharedPreference();
        getIntent().putExtra(LANDING_PAGE_USER_ID, LOGGED_OUT);

        startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext()));
    }

    private void updateSharedPreference(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferenceEditor = sharedPreferences.edit();
        sharedPreferenceEditor.putInt(getString(R.string.preference_userid_key), LOGGED_OUT);
        sharedPreferenceEditor.apply();
    }

    static Intent landingPageIntentFactory(Context context, int userId){
        Intent intent = new Intent(context, LandingPageActivity.class);
        intent.putExtra(LANDING_PAGE_USER_ID, userId);
        return intent;
    }
}