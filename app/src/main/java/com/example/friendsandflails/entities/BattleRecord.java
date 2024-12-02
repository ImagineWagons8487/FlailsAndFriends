package com.example.friendsandflails.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.friendsandflails.database.FlailDatabase;

import java.time.LocalDate;


@Entity(tableName = FlailDatabase.RECORD_TABLE)
public class BattleRecord {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String title;
    private String record;
    private int date;

    public BattleRecord(int userId, String title, String record) {
        this.userId = userId;
        this.title = title;
        this.record = record;
        this.date = localDateToInt(LocalDate.now());
    }

    private int localDateToInt(LocalDate date){
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        return year * 10000 + month * 100 + day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public int getDate() {return date;}

    public void setDate(int date){this.date = date;}

}