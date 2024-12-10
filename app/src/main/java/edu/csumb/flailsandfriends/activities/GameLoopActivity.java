package edu.csumb.flailsandfriends.activities;

import android.os.Bundle;
import android.view.SurfaceHolder;
import androidx.appcompat.app.AppCompatActivity;

public class GameLoopActivity extends AppCompatActivity {
    private GameView gameView;
    private GameThread gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);
        setContentView(gameView);
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