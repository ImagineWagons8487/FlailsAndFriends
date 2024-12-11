package edu.csumb.flailsandfriends.activities;

public interface GameMessageCallback {
    void onGameMessage(String message);
    void onGameEnd(String battleLog);
}