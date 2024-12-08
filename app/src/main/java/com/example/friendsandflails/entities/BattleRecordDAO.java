package edu.csumb.flailsandfriends.entities;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import edu.csumb.flailsandfriends.database.FlailDatabase;

import java.util.List;

@Dao
public interface BattleRecordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BattleRecord... battleRecord);

    @Delete
    void delete(BattleRecord battleRecord);

    @Query("SELECT * FROM " + FlailDatabase.RECORD_TABLE + " ORDER BY date")
    LiveData<List<BattleRecord>> getAllRecords();

    @Query("DELETE from " + FlailDatabase.RECORD_TABLE)
    void deleteAll();

    @Query("SELECT * from " + FlailDatabase.RECORD_TABLE + " WHERE title == :title")
    LiveData<BattleRecord> getBattleByTitle(String title);

    @Query("SELECT * from " + FlailDatabase.RECORD_TABLE + " WHERE id == :id")
    LiveData<BattleRecord> getBattleById(int id);
}