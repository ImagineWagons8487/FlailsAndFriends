package edu.csumb.flailsandfriends.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.databinding.ActivityCombatBinding;

import edu.csumb.flailsandfriends.entities.User;

import java.util.Random;



public class CombatActivity extends AppCompatActivity {
    // Enum for player and CPU selections
    private static CombatRPS player_selection = CombatRPS.SCISSORS;
    private static CombatRPS cpu_selection = CombatRPS.ROCK;

// NEED THESE
    public int player_health = 100;
    public int cpu_health = 100;

    // Flags for match outcome
    public static boolean player_winner = true;
    public static boolean match_draw = false;
    public static boolean fight = true;

    private ActivityCombatBinding binding;

    private FlailRepo repository;

    private User user;
    // Instance of the new CombatGame class to handle game logic
    private CombatGame combatGame = new CombatGame();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout
        binding = ActivityCombatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Initialize repository
        repository = FlailRepo.getRepository(getApplication());

        // Set button listeners for user actions
        binding.heavyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Player chooses ROCK
                player_selection = CombatRPS.ROCK;
                proceed(); // Process turn
            }
        });

        binding.lightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Player chooses PAPER
                player_selection = CombatRPS.PAPER;
                proceed(); // Process turn
            }
        });

        binding.dodgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player_selection = CombatRPS.SCISSORS;
                proceed();
            }
        });
    }
    // Added: Updated proceed method to utilize turnDamage logic
    private void proceed() {
        //get random cpu selection
        cpu_selection = getRandomCPUSelection();
        //get winner based on cpu_selection and player_selection
        checkWinner();
        // Handle damage and check for game-over state
        turnDamage(player_winner);
    }

    //function returns random value
    private CombatRPS getRandomCPUSelection() {
        int random = new Random().nextInt(3);

        switch (random){
            case 0: return CombatRPS.HEAVY;
            case 1: return CombatRPS.LIGHT;
            case 2: return CombatRPS.DODGE;
        }
        return CombatRPS.HEAVY;
    }
    // Updated: Logic to determine if the player or CPU wins
    private void checkWinner() { //process that decides who wins the turn
        if (player_selection == cpu_selection) {
            match_draw = true; // It's a tie
            return;
        }


        // Determine winner based on RPS rules
        if (player_selection == CombatRPS.ROCK) {
            player_winner = cpu_selection != CombatRPS.PAPER;
        } else if (player_selection == CombatRPS.PAPER) {
            player_winner = cpu_selection != CombatRPS.SCISSORS;
        } else if (player_selection == CombatRPS.SCISSORS) {
            player_winner = cpu_selection != CombatRPS.ROCK;
        }

        // Log the selections for debugging
        Log.e("cpu selected", cpu_selection.toString());
        Log.e("player selected", player_selection.toString());
    }

    //Turn results and damage
    private void turnDamage(boolean player_winner) {
        int damage = 20; // Example fixed damage per turn
        combatGame.dealDamage(player_winner, damage); // Pass the result to CombatGame for health updates

        // Check if the game is over
        if (combatGame.checkForGameOver()) {
            // Transition to GameOverActivity
            Intent intent = new Intent(CombatActivity.this, GameOverActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        }

    }

    public int damageCalc(){
        int defaultDamage;
        defaultDamage = 20;
        //TODO: Add calculation based on equipment (again, if we have those)

        return defaultDamage;
    }

    private int healthLeft(int health){
        health = health - damageCalc();
        return health - damageCalc();
    }

    boolean battleState(boolean fight){
        if(player_health > 0 && cpu_health > 0){
            fight = true;
            proceed();
        }
        else{
            fight = false;
        }
        return fight;
    }

    public void endMatch(){
        if(cpu_health <= 0){
            System.out.println("You Win!\n");
            //TODO: Add a way for it to send results to battle log

            startActivity(PostMatchActivity.postMatchIntentFactory(getApplicationContext()));
        }

        if(player_health <= 0){
            System.out.println("You Lose!");
            //TODO: Add a way for it to send results to battle log

            startActivity(PostMatchActivityLose.postMatchLoseIntentFactory(getApplicationContext()));
        }
    }

    public void clearInputs(){
        player_selection = null;
        cpu_selection = null;
        match_draw = false;
        player_winner = true;
    }


    public static Intent combatActivityIntentFactory(Context context) {
        return new Intent(context, CombatActivity.class);
    }
    }