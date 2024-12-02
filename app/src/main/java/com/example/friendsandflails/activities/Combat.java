package com.example.friendsandflails.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import com.example.friendsandflails.databinding.LayoutCombatBinding;

import java.util.Random;

public class Combat extends AppCompatActivity{
    //default values
    private static CombatRPS player_selection = CombatRPS.SCISSORS;
    private static CombatRPS cpu_selection = CombatRPS.ROCK;
    private final int player_health = 100;
    private final int cpu_health = 100;
    private LayoutCombatBinding binding;

    private static boolean player_winner = true;
    private static boolean match_draw = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LayoutCombatBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.heavyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player_selection = CombatRPS.ROCK;
            }
        });

        binding.lightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player_selection = CombatRPS.PAPER;
            }
        });

        binding.dodgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player_selection = CombatRPS.SCISSORS;
            }
        });

        proceed();
    }

    private void proceed() {
        //get random cpu selection
        cpu_selection = getRandomCPUSelection();
        //get winner based on cpu_selection and user_selection
        checkWinner();
        //print result based on result
        turnDamage();
    }

    //function returns random value
    private CombatRPS getRandomCPUSelection() {
        int random = new Random().nextInt(3);

        switch (random){
            case 0: return CombatRPS.ROCK;
            case 1: return CombatRPS.PAPER;
            case 2: return CombatRPS.SCISSORS;
        }
        return CombatRPS.ROCK;
    }

    private void checkWinner() { //process that decides who wins the turn

        if(player_selection == cpu_selection){
            match_draw = true;
            return;
        }

        //rock beats scissors, loses to paper
        if(player_selection == CombatRPS.ROCK){
            if(cpu_selection == CombatRPS.PAPER){
                player_winner = false;
                return;
            }
            else if(cpu_selection == CombatRPS.SCISSORS){
                player_winner = true;
                return;
            }
        }

        //paper beats rock, loses to scissors
        if(player_selection == CombatRPS.PAPER){
            if(cpu_selection == CombatRPS.SCISSORS){
                player_winner = false;
                return;
            }
            else if(cpu_selection == CombatRPS.ROCK){
                player_winner = true;
                return;
            }
        }

        //scissors beats paper, loses to rock
        if(player_selection == CombatRPS.SCISSORS){
            if(cpu_selection == CombatRPS.ROCK){
                player_winner = false;
                return;
            }
            else if(cpu_selection == CombatRPS.PAPER){
                player_winner = true;
                return;
            }
        }

        Log.e("cpu selected", cpu_selection.toString());
        Log.e("player selected", player_selection.toString());
    }

    //Turn results and damage
    private void turnDamage(){
        System.out.println("Player selected: " + player_selection + "\n");
        System.out.println("CPU selected: " + cpu_selection + "\n");

        String result;

        if(match_draw){
            result = "Same choice! Your attacks clashed!";
        }
        else{
            if(player_winner) {
                result = "Your attack was successful!";
                //TODO: Add calculation for damage amount based on player's weapons and enemy's gear (if we have those)
                System.out.println(result);
                System.out.println("You dealt " + damageCalc() + " damage!");
                System.out.println("Health: " + healthLeft(cpu_health));
            }
            else {
                result = "Your attack failed!\nYour opponent counterattacks!";
                //TODO: Add calculation for damage amount based on enemy's weapons and player's gear (if we have those)
                System.out.println(result);
                System.out.println("You took " + damageCalc() + " damage!");
                System.out.println("Health: " + healthLeft(player_health));
            }
        }
    }

    private int damageCalc(){
        int defaultDamage = 20;
        //TODO: Add calculation based on equipment (again, if we have those)

        return defaultDamage;
    }

    private int healthLeft(int health){
        return health - damageCalc();
    }
    //reset values after the result is displayed to user
    private void resetValues() {
        player_selection = CombatRPS.SCISSORS;
        cpu_selection = CombatRPS.ROCK;

        player_winner = true;
        match_draw = false;
        Log.e(MainActivity.class.getName(), "values reset successful");
    }
}

