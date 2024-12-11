package edu.csumb.flailsandfriends.activities;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    private static final int TARGET_FPS = 60;  // Target frame rate
    private boolean running;
    private SurfaceHolder surfaceHolder;
    private long lastTime;
    private GameView gameView;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        this.lastTime = System.nanoTime();
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        while (running) {
            long now = System.nanoTime();
            long elapsedTime = now - lastTime;

            // Check if the frame is ready to be updated and rendered
            if (elapsedTime >= 1000000000 / TARGET_FPS) {
                gameView.update();  // Update game state
                render();            // Render the game
                lastTime = now;
            }

            try {
                Thread.sleep(10);  // Prevent the loop from running too fast
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void render() {
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                synchronized (surfaceHolder) {
                    gameView.render(canvas);  // Call render on the GameView
                }
            }
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
