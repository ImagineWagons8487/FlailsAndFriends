package edu.csumb.flailsandfriends.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import edu.csumb.flailsandfriends.database.FlailDatabase;


@Entity(tableName = FlailDatabase.EQUIPMENT_TABLE)
public class Equipment {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String equipmentName;
    private int buff;
    private String bitmapName;

    public Equipment(String equipmentName, String bitmapName, int buff) {
        this.equipmentName = equipmentName;
        this.bitmapName = bitmapName;
        this.buff = buff;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String username) {
        this.equipmentName = username;
    }

    public String getBitmapName(){return bitmapName;}

    public void setBitmapName(String bitmapName){this.bitmapName = bitmapName;}

    public int getBuff() {
        return buff;
    }

    public void setBuff(int buff) {
        this.buff = buff;
    }

}