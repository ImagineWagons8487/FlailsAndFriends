package edu.csumb.flailsandfriends.entities;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import edu.csumb.flailsandfriends.database.FlailDatabase;

import java.util.List;

@Dao
public interface EquipmentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Equipment... equipment);

    @Delete
    void delete(Equipment... equipment);

    @Update
    void update(Equipment... equipment);

    @Query("SELECT * FROM " + FlailDatabase.EQUIPMENT_TABLE + " ORDER BY equipmentName")
    LiveData<List<Equipment>> getAllEquipments();

    @Query("DELETE from " + FlailDatabase.EQUIPMENT_TABLE)
    void deleteAll();

    @Query("SELECT * from " + FlailDatabase.EQUIPMENT_TABLE + " WHERE equipmentName == :equipmentName")
    Equipment getEquipmentByNameOffline(String equipmentName);

    @Query("SELECT * from " + FlailDatabase.EQUIPMENT_TABLE + " WHERE equipmentName == :equipmentName")
    LiveData<Equipment> getEquipmentByName(String equipmentName);

    @Query("SELECT * from " + FlailDatabase.EQUIPMENT_TABLE + " WHERE id == :equipmentId")
    LiveData<List<Equipment>> getEquipmentById(int equipmentId);
}
