package edu.csumb.flailsandfriends.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Objects;

import edu.csumb.flailsandfriends.R;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameMessageCallback messageCallback;
    private GameThread thread;
    private Paint textPaint;

    private Bitmap rockBitmap;
    private Bitmap paperBitmap;
    private Bitmap scissorsBitmap;
    private Bitmap backgroundBitmap;

    private final Rect rockBounds = new Rect();
    private final Rect paperBounds = new Rect();
    private final Rect scissorsBounds = new Rect();

    private static final float BUTTON_SCALE_PRESSED = 0.9f;
    private static final long PRESS_ANIMATION_DURATION = 150;

    private String pressedButton = null;
    private long pressStartTime = 0;


    private int userScore = 0;
    private int cpuScore = 0;
    private boolean roShamBo = true;
    final private String ROCK = "ROCK";
    final private String PAPER = "PAPER";
    final private String SCISSORS = "SCISSORS";
    final private String[] selections = {ROCK, PAPER, SCISSORS};
    public String battleLog = "";

    private HashMap<String, String> outcomeMap;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(60);
        textPaint.setTextAlign(Paint.Align.CENTER);

        outcomeMap = new HashMap<>();
        outcomeMap.put(PAPER, ROCK);
        outcomeMap.put(ROCK, SCISSORS);
        outcomeMap.put(SCISSORS, PAPER);

        userScore = 0;
        cpuScore = 0;

    }

    public void setMessageCallback(GameMessageCallback callback) {
        this.messageCallback = callback;
    }

    private void showMessage(String message) {
        if (messageCallback != null) {
            messageCallback.onGameMessage(message);
        }
    }

    private void initializeBitmaps() {
        //loads base bitmaps
        rockBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rockbutton);
        paperBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.paperbutton);
        scissorsBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scissorsbutton);
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);

        // scales bitmaps
        rockBitmap = Bitmap.createScaledBitmap(rockBitmap, rockBounds.width(), rockBounds.height(), true);
        paperBitmap = Bitmap.createScaledBitmap(paperBitmap, paperBounds.width(), paperBounds.height(), true);
        scissorsBitmap = Bitmap.createScaledBitmap(scissorsBitmap, scissorsBounds.width(), scissorsBounds.height(), true);
        backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, getWidth(), getHeight(), true);
    }

    /**
     *  Creates the games thread after the SurfaceView is created
     * **/
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new GameThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    /**
     * Is called after any screen change. Bitmaps are initialized here because
     * otherwise they don't have size references and return null.
     * **/
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        cleanup();

        int buttonWidth = width / 3;
        int buttonHeight = height / 6;
        int padding = 125;
        int y = height - buttonHeight - padding;

        rockBounds.set(0, y, buttonWidth, y + buttonHeight);
        paperBounds.set(buttonWidth, y, buttonWidth * 2, y + buttonHeight);
        scissorsBounds.set(buttonWidth * 2, y, width, y + buttonHeight);

        initializeBitmaps();
    }

    /**
     * Similar to onPause in GameLoopActivity
     * **/

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        cleanup();
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This function is called after any touch event. Currently checks
     * if touch events take place on screen positions corresponding to
     * buttons and acts accordingly
     * **/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (roShamBo) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    if (rockBounds.contains(x, y)) {
                        pressedButton = ROCK;
                        pressStartTime = System.currentTimeMillis();
                    } else if (paperBounds.contains(x, y)) {
                        pressedButton = PAPER;
                        pressStartTime = System.currentTimeMillis();
                    } else if (scissorsBounds.contains(x, y)) {
                        pressedButton = SCISSORS;
                        pressStartTime = System.currentTimeMillis();
                    }
                    break;

                case MotionEvent.ACTION_UP:

                    if (pressedButton != null) {
                        if (rockBounds.contains(x, y) && pressedButton.equals(ROCK)) {
                            shoot(ROCK);
                        } else if (paperBounds.contains(x, y) && pressedButton.equals(PAPER)) {
                            shoot(PAPER);
                        } else if (scissorsBounds.contains(x, y) && pressedButton.equals(SCISSORS)) {
                            shoot(SCISSORS);
                        }
                        pressedButton = null;
                    }
                    break;

                case MotionEvent.ACTION_MOVE:

                    if (pressedButton != null) {
                        Rect currentBounds = null;
                        if (pressedButton.equals(ROCK)) currentBounds = rockBounds;
                        else if (pressedButton.equals(PAPER)) currentBounds = paperBounds;
                        else if (pressedButton.equals(SCISSORS)) currentBounds = scissorsBounds;

                        if (currentBounds != null && !currentBounds.contains(x, y)) {
                            pressedButton = null;
                        }
                    }
                    break;
            }
        }

        return true;
    }

    private void shoot(String selection){
        String cpuSelection = selections[(int) (Math.random() * 3)];
        if(Objects.equals(outcomeMap.get(selection), cpuSelection)){
            userScore++;
        }else if(Objects.equals(outcomeMap.get(cpuSelection), selection)){
            cpuScore++;
        }
        battleLog += " " + selection + cpuSelection;
        showMessage(String.format("You:%s VS Them:%s", selection, cpuSelection));
        checkScore();
    }

    private void checkScore(){
        if(userScore == 5 || cpuScore == 5){
            roShamBo = false;
            if (messageCallback != null) {
                messageCallback.onGameEnd(battleLog);
            }
        }
    }

    public void update() {
    }

    public void cleanup() {

        if (backgroundBitmap != null) {
            backgroundBitmap.recycle();
        }
        if (rockBitmap != null) {
            rockBitmap.recycle();
        }
        if (paperBitmap != null) {
            paperBitmap.recycle();
        }
        if (scissorsBitmap != null) {
            scissorsBitmap.recycle();
        }
    }

    public void render(Canvas canvas) {
        if (canvas != null) {
            if (backgroundBitmap != null && !backgroundBitmap.isRecycled()) {
                canvas.drawBitmap(backgroundBitmap, 0, 0, null);
            } else {
                canvas.drawColor(Color.BLACK);
            }

            if (roShamBo) {
                drawButton(canvas, rockBitmap, rockBounds, ROCK, "ROCK");
                drawButton(canvas, paperBitmap, paperBounds, PAPER, "PAPER");
                drawButton(canvas, scissorsBitmap, scissorsBounds, SCISSORS, "SCISSORS");

                canvas.drawText(String.format("Your Score: %d CPU Score: %d", userScore, cpuScore), canvas.getWidth() / 2,
                        rockBounds.top - 100, textPaint);
            }else{
                if(userScore == 5){
                    canvas.drawText(String.format("Your Score: %d You Win!", userScore), canvas.getWidth() / 2,
                            rockBounds.top - 100, textPaint);
                }else{
                    canvas.drawText(String.format("CPU Score: %d You Lose!",cpuScore), canvas.getWidth() / 2,
                            rockBounds.top - 100, textPaint);
                }

            }
        }
    }

    private void drawButton(Canvas canvas, Bitmap bitmap, Rect bounds, String buttonType, String label) {
        if (bitmap != null && !bitmap.isRecycled()) {
            float scale = 1.0f;

            if (buttonType.equals(pressedButton)) {
                long elapsed = System.currentTimeMillis() - pressStartTime;

                if (elapsed <= PRESS_ANIMATION_DURATION) {
                    float progress = (float) elapsed / PRESS_ANIMATION_DURATION;
                    scale = 1.0f - ((1.0f - BUTTON_SCALE_PRESSED) * progress);
                } else {
                    scale = BUTTON_SCALE_PRESSED;
                }
            }

            int scaledWidth = (int) (bounds.width() * scale);
            int scaledHeight = (int) (bounds.height() * scale);
            int offsetX = (bounds.width() - scaledWidth) / 2;
            int offsetY = (bounds.height() - scaledHeight) / 2;

            Rect drawBounds = new Rect(
                    bounds.left + offsetX,
                    bounds.top + offsetY,
                    bounds.left + offsetX + scaledWidth,
                    bounds.top + offsetY + scaledHeight
            );

            canvas.drawBitmap(bitmap, null, drawBounds, null);
            canvas.drawText(label, bounds.centerX(), bounds.centerY() + bounds.height(), textPaint);
        }
    }
}