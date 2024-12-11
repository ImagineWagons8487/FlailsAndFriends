package edu.csumb.flailsandfriends.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.google.android.material.snackbar.Snackbar;

import edu.csumb.flailsandfriends.R;
import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.entities.User;

public class GameLoopActivity extends AppCompatActivity implements GameMessageCallback {
    private static final String GAME_LOOP_USER_ID = "edu.csumb.flailsandfriends.GAME_LOOP_USER_ID";

    static final String SAVED_INSTANCE_STATE_USERID_KEY = "edu.csumb.flailsandfriends.SAVED_INSTANCE_STATE_USERID_KEY";

    private static final int LOGGED_OUT = -1;

    private FlailRepo repository;

    public static final String TAG = "FLAIL_FRIENDS";

    private int loggedInUserId = -1;

    private User user;

    private GameView gameView;
    private GameThread gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);
        gameView.setMessageCallback(this);
        setContentView(gameView);

        repository = FlailRepo.getRepository(getApplication());

        logInUser(savedInstanceState);

        if(loggedInUserId == LOGGED_OUT){
            Intent intent = LoginActivity.logInIntentFactory(getApplicationContext());
            startActivity(intent);
        }
        updateSharedPreference();
    }

    @Override
    public void onGameMessage(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackbarText = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);

        snackbarText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbarText.setGravity(Gravity.CENTER_HORIZONTAL);

        snackbarText.setMaxLines(10);
        snackbarText.setTextSize(16);
        snackbar.show();
    }

    @Override
    public void onGameEnd(String battleLog) {
        startActivity(GameOverActivity.gameOverIntentFactory(getApplicationContext(), battleLog));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceHolder holder = gameView.getHolder();
        gameThread = new GameThread(holder, gameView);
        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameThread != null) {
            gameThread.setRunning(false);
            boolean retry = true;
            while (retry) {
                try {
                    gameThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameThread != null) {
            gameThread.setRunning(false);
        }
    }

    public static Intent gameLoopIntentFactory(Context context, int userId){
        Intent intent = new Intent(context, GameLoopActivity.class);
        intent.putExtra(GAME_LOOP_USER_ID, userId);
        return intent;
    }

    private void logInUser(Bundle savedInstanceState){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userid_key), LOGGED_OUT);

        if(loggedInUserId == LOGGED_OUT & savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)){
            loggedInUserId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT){
            loggedInUserId = getIntent().getIntExtra(GAME_LOOP_USER_ID, LOGGED_OUT);
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
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(GameLoopActivity.this);
        final AlertDialog alertDialog = alertBuilder.create();

        alertBuilder.setMessage("Logout");

        alertBuilder.setPositiveButton("Logout", (dialogInterface, i) -> logout());

        alertBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> alertDialog.dismiss());

        alertBuilder.create().show();
    }

    private void logout(){
        loggedInUserId = LOGGED_OUT;
        updateSharedPreference();
        getIntent().putExtra(GAME_LOOP_USER_ID, LOGGED_OUT);

        startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext()));
    }

    private void updateSharedPreference(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferenceEditor = sharedPreferences.edit();
        sharedPreferenceEditor.putInt(getString(R.string.preference_userid_key), LOGGED_OUT);
        sharedPreferenceEditor.apply();
    }
}