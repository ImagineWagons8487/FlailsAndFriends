package edu.csumb.flailsandfriends.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import edu.csumb.flailsandfriends.R;
import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.databinding.ActivityGameOverBinding;
import edu.csumb.flailsandfriends.databinding.ActivityLandingPageBinding;
import edu.csumb.flailsandfriends.entities.User;

public class GameOverActivity extends AppCompatActivity {
    private static final String GAME_OVER_USER_ID = "edu.csumb.flailsandfriends.GAME_OVER_USER_ID";

    private static final String GAME_OVER_RECORD = "edu.csumb.flailsandfriends.GAME_OVER_RECORD";

    static final String SAVED_INSTANCE_STATE_USERID_KEY = "edu.csumb.flailsandfriends.SAVED_INSTANCE_STATE_USERID_KEY";

    private static final int LOGGED_OUT = -1;

    private FlailRepo repository;

    public static final String TAG = "FLAIL_FRIENDS";

    private int loggedInUserId = -1;

    private User user;

    private ActivityGameOverBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameOverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the repository for database interaction
        repository = FlailRepo.getRepository(getApplication());

        logInUser(savedInstanceState);

        if(loggedInUserId == LOGGED_OUT){
            Intent intent = LoginActivity.logInIntentFactory(getApplicationContext());
            startActivity(intent);
        }
        updateSharedPreference();


        // Set up button listeners
        binding.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(GameLoopActivity.gameLoopIntentFactory(getApplicationContext(), loggedInUserId));
            }
        });

        binding.quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quit the game
                startActivity(LandingPageActivity.landingPageIntentFactory(getApplicationContext(), loggedInUserId));
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
    public static Intent gameOverIntentFactory(Context context, int userId, String battleLog) {
        Intent intent = new Intent(context, GameOverActivity.class);
        intent.putExtra(GAME_OVER_RECORD, battleLog);
        intent.putExtra(GAME_OVER_USER_ID, userId);
        return intent;
    }

    private void logInUser(Bundle savedInstanceState){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userid_key), LOGGED_OUT);

        if(loggedInUserId == LOGGED_OUT & savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)){
            loggedInUserId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT){
            loggedInUserId = getIntent().getIntExtra(GAME_OVER_USER_ID, LOGGED_OUT);
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
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(GameOverActivity.this);
        final AlertDialog alertDialog = alertBuilder.create();

        alertBuilder.setMessage("Logout");

        alertBuilder.setPositiveButton("Logout", (dialogInterface, i) -> logout());

        alertBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> alertDialog.dismiss());

        alertBuilder.create().show();
    }

    private void logout(){
        loggedInUserId = LOGGED_OUT;
        updateSharedPreference();
        getIntent().putExtra(GAME_OVER_USER_ID, LOGGED_OUT);

        startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext()));
    }

    private void updateSharedPreference(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferenceEditor = sharedPreferences.edit();
        sharedPreferenceEditor.putInt(getString(R.string.preference_userid_key), LOGGED_OUT);
        sharedPreferenceEditor.apply();
    }
}

