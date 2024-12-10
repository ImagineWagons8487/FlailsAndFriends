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

import edu.csumb.flailsandfriends.R;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread thread;
    private Paint textPaint;
    private Paint buttonPaint;

    private Bitmap rockBitmap;
    private Bitmap paperBitmap;
    private Bitmap scissorsBitmap;
    private Bitmap backgroundBitmap;

    // Game state
    private enum GameState {SELECTING, RESULT}

    private enum Choice {ROCK, PAPER, SCISSORS, NONE}

    private GameState currentState = GameState.SELECTING;
    private Choice playerChoice = Choice.NONE;
    private Choice computerChoice = Choice.NONE;
    private String resultText = "";

    // Button regions
    private Rect rockButton = new Rect();
    private Rect paperButton = new Rect();
    private Rect scissorsButton = new Rect();

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);

        // Initialize paints
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(60);
        textPaint.setTextAlign(Paint.Align.CENTER);

        buttonPaint = new Paint();
        buttonPaint.setColor(Color.BLUE);

    }

    private void initializeBitmaps() {
        //loads base bitmaps
        rockBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.heavy);
        paperBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.light);
        scissorsBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dodge);
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);

        // scales bitmaps
        rockBitmap = Bitmap.createScaledBitmap(rockBitmap, rockButton.width(), rockButton.height(), true);
        paperBitmap = Bitmap.createScaledBitmap(paperBitmap, paperButton.width(), paperButton.height(), true);
        scissorsBitmap = Bitmap.createScaledBitmap(scissorsBitmap, scissorsButton.width(), scissorsButton.height(), true);
        backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, getWidth(), getHeight(), true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new GameThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        cleanup();

        int buttonWidth = width / 3;
        int buttonHeight = height / 6;
        int padding = 50;
        int y = height - buttonHeight - padding;

        rockButton.set(0, y, buttonWidth, y + buttonHeight);
        paperButton.set(buttonWidth, y, buttonWidth * 2, y + buttonHeight);
        scissorsButton.set(buttonWidth * 2, y, width, y + buttonHeight);

        initializeBitmaps();
    }

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return true;
        }

        int x = (int) event.getX();
        int y = (int) event.getY();

        if (currentState == GameState.SELECTING) {
            if (rockButton.contains(x, y)) {
                makeChoice(Choice.ROCK);
            } else if (paperButton.contains(x, y)) {
                makeChoice(Choice.PAPER);
            } else if (scissorsButton.contains(x, y)) {
                makeChoice(Choice.SCISSORS);
            }
        } else if (currentState == GameState.RESULT) {
            // Reset game on touch during result screen
            resetGame();
        }

        return true;
    }

    private void makeChoice(Choice choice) {
        playerChoice = choice;
        computerChoice = getRandomChoice();
        determineWinner();
        currentState = GameState.RESULT;
    }

    private Choice getRandomChoice() {
        int random = (int) (Math.random() * 3);
        switch (random) {
            case 0:
                return Choice.ROCK;
            case 1:
                return Choice.PAPER;
            default:
                return Choice.SCISSORS;
        }
    }

    private void determineWinner() {
        if (playerChoice == computerChoice) {
            resultText = "It's a tie!";
            return;
        }

        boolean playerWins =
                (playerChoice == Choice.ROCK && computerChoice == Choice.SCISSORS) ||
                        (playerChoice == Choice.PAPER && computerChoice == Choice.ROCK) ||
                        (playerChoice == Choice.SCISSORS && computerChoice == Choice.PAPER);

        resultText = playerWins ? "You win!" : "Computer wins!";
    }

    private void resetGame() {
        currentState = GameState.SELECTING;
        playerChoice = Choice.NONE;
        computerChoice = Choice.NONE;
        resultText = "";
    }

    public void update() {
        // Update game state if needed
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

            if (currentState == GameState.SELECTING) {
                // Add null checks for all bitmaps
                if (rockBitmap != null && !rockBitmap.isRecycled()) {
                    canvas.drawBitmap(rockBitmap, rockButton.left, rockButton.top, null);
                }
                if (paperBitmap != null && !paperBitmap.isRecycled()) {
                    canvas.drawBitmap(paperBitmap, paperButton.left, paperButton.top, null);
                }
                if (scissorsBitmap != null && !scissorsBitmap.isRecycled()) {
                    canvas.drawBitmap(scissorsBitmap, scissorsButton.left, scissorsButton.top, null);
                }

                // Draw button labels
                float textY = rockButton.centerY() + rockButton.height();
                canvas.drawText("ROCK", rockButton.centerX(), textY, textPaint);
                canvas.drawText("PAPER", paperButton.centerX(), textY, textPaint);
                canvas.drawText("SCISSORS", scissorsButton.centerX(), textY, textPaint);

                // Draw instructions
                canvas.drawText("Choose your move!", canvas.getWidth() / 2,
                        rockButton.top - 100, textPaint);
            } else {
                // Rest of your result screen code remains the same
                int centerX = canvas.getWidth() / 2;
                int centerY = canvas.getHeight() / 2;

                canvas.drawText("Your choice: " + playerChoice,
                        centerX, centerY - 200, textPaint);
                canvas.drawText("Computer's choice: " + computerChoice,
                        centerX, centerY - 100, textPaint);
                canvas.drawText(resultText, centerX, centerY, textPaint);
                canvas.drawText("Tap to play again!", centerX, centerY + 100, textPaint);
            }
        }
    }
}