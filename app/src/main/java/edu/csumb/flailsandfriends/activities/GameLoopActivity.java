package edu.csumb.flailsandfriends.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class GameLoopActivity extends AppCompatActivity implements GameMessageCallback {
    private GameView gameView;
    private GameThread gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);
        gameView.setMessageCallback(this);
        setContentView(gameView);
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
}