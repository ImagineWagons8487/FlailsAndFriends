package edu.csumb.flailsandfriends.activities;

public class CombatGame {
    private int player_health = 100;
    private int cpu_health = 100;

    public boolean checkForGameOver() {
        return player_health <= 0 || cpu_health <= 0;
    }

    public void dealDamage(boolean playerWins, int damage) {
        if (playerWins) {
            cpu_health = Math.max(0, cpu_health - damage);
        } else {
            player_health = Math.max(0, player_health - damage);
        }
    }

    public int getPlayerHealth() {
        return player_health;
    }

    public int getCpuHealth() {
        return cpu_health;
    }
}

