package com.example.friendsandflails.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.friendsandflails.database.FlailDatabase;

@Entity(tableName = FlailDatabase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String password;
    private boolean isAdmin;
    private String inventory = "";

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        isAdmin = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getInventory() { return inventory; }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public void addToInventory(int equipmentId) { inventory = inventory + " " + equipmentId; }
}
