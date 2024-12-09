package edu.csumb.flailsandfriends.entities;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import edu.csumb.flailsandfriends.database.FlailDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Delete
    void delete(User... user);

    @Update
    void update(User... user);

    @Query("SELECT * FROM " + FlailDatabase.USER_TABLE + " ORDER BY username")
    LiveData<List<User>> getAllUser();

    @Query("DELETE from " + FlailDatabase.USER_TABLE)
    void deleteAll();

    // username column should be called email
    // but we're not changing it for now so that we avoid a database migration.
    @Query("SELECT * from " + FlailDatabase.USER_TABLE + " WHERE username == :email")
    LiveData<User> getUserByEmail(String email);

    @Query("SELECT * from " + FlailDatabase.USER_TABLE + " WHERE id == :loggedInUserId")
    LiveData<User> getUserByUserId(int loggedInUserId);
}
