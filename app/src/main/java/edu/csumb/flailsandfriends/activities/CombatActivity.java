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

    private static CombatRPS player_selection;
    private static CombatRPS cpu_selection;
    public static int player_health = 100;
    public static int cpu_health = 100;

    public static boolean player_winner = true;
    public static boolean match_draw = false;
    public static boolean fight = true;

    private ActivityCombatBinding binding;

    private FlailRepo repository;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCombatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = FlailRepo.getRepository(getApplication());



        binding.heavyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player_selection = CombatRPS.HEAVY;
            }
        });

        binding.lightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player_selection = CombatRPS.LIGHT;
            }
        });

        binding.dodgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player_selection = CombatRPS.DODGE;
            }
        });

        binding.proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceed();
            }
        });
    }


    private void proceed() {
        //get random cpu selection
        cpu_selection = getRandomCPUSelection();
        //get winner based on cpu_selection and player_selection
        checkWinner();
        //print result based on result()
        turnDamage();
        //Reset all values related to inputs
        clearInputs();

        //Note: This broke the program so everything
        // related to the process is commented out for now
        //TODO: Add way for it to end the game when
        // health reaches 0, preferably WITHOUT exploding.
        if(player_health <= 0 || cpu_health <= 0){
            endMatch();
        }
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

    private void checkWinner() { //process that decides who wins the turn

        if(player_selection == cpu_selection){
            match_draw = true;
            return;
        }

        //rock (Heavy) beats scissors (Dodge), loses to paper (Light)
        if(player_selection == CombatRPS.HEAVY){
            if(cpu_selection == CombatRPS.LIGHT){
                player_winner = false;
                return;
            }
            else if(cpu_selection == CombatRPS.DODGE){
                player_winner = true;
                return;
            }
        }

        //paper (Light) beats rock (Heavy), loses to scissors (Dodge)
        if(player_selection == CombatRPS.LIGHT){
            if(cpu_selection == CombatRPS.DODGE){
                player_winner = false;
                return;
            }
            else if(cpu_selection == CombatRPS.HEAVY){
                player_winner = true;
                return;
            }
        }

        //scissors (Dodge) beats paper (Light), loses to rock (Heavy)
        if(player_selection == CombatRPS.DODGE){
            if(cpu_selection == CombatRPS.HEAVY){
                player_winner = false;
                return;
            }
            else if(cpu_selection == CombatRPS.LIGHT){
                player_winner = true;
                return;
            }
        }

        Log.e("cpu selected", cpu_selection.toString());
        Log.e("player selected", player_selection.toString());
    }

    //Turn results and damage
    private void turnDamage() {
        System.out.println("Player selected: " + player_selection + "\n");
        System.out.println("CPU selected: " + cpu_selection + "\n");

        String result;

        if (match_draw) {
            result = "Same choice! Your attacks clashed!";
            System.out.println(result);
            System.out.println("Player Health: " + player_health);
            System.out.println("Enemy Health: " + cpu_health);

        } else {
            if (player_winner) {
                result = "Your attack was successful!";
                //TODO: Add calculation for damage amount based on player's weapons and enemy's gear (if we have those)
                System.out.println(result);
                System.out.println("You dealt " + damageCalc() + " damage!");
                cpu_health = cpu_health - damageCalc();
                System.out.println("Player Health: " + player_health);
                System.out.println("Enemy Health: " + cpu_health);

            } else {
                result = "Your attack failed!\nYour opponent counterattacks!";
                //TODO: Add calculation for damage amount based on enemy's weapons and player's gear (if we have those)
                System.out.println(result);
                System.out.println("You took " + damageCalc() + " damage!");
                player_health = player_health - damageCalc();
                System.out.println("Player Health: " + player_health);
                System.out.println("Enemy Health: " + cpu_health);
            }
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




    static Intent combatActivityIntentFactory(Context context) {
        return new Intent(context, CombatActivity.class);
    }
}
