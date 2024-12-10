package edu.csumb.flailsandfriends.activities;


import org.junit.Test;
import static org.junit.Assert.*;

public class CombatGameTest {

    @Test
    public void testPlayerHealthReachesZero() {
        CombatGame game = new CombatGame();
        game.dealDamage(false, 100); // Player takes full damage

        assertTrue("Game should end when player health is 0", game.checkForGameOver());
    }

    @Test
    public void testCpuHealthReachesZero() {
        CombatGame game = new CombatGame();
        game.dealDamage(true, 100); // CPU takes full damage

        assertTrue("Game should end when CPU health is 0", game.checkForGameOver());
    }
}


