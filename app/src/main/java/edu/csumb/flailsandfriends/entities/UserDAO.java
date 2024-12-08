package edu.csumb.flailsandfriends.entities;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import edu.csumb.flailsandfriends.database.FlailDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + FlailDatabase.USER_TABLE + " ORDER BY username")
    LiveData<List<User>> getAllUser();

    @Query("DELETE from " + FlailDatabase.USER_TABLE)
    void deleteAll();

    @Query("SELECT * from " + FlailDatabase.USER_TABLE + " WHERE username == :username")
    LiveData<User> getUserByUsername(String username);

    @Query("SELECT * from " + FlailDatabase.USER_TABLE + " WHERE id == :loggedInUserId")
    LiveData<User> getUserByUserId(int loggedInUserId);
}
