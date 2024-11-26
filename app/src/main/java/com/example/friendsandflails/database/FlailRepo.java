package com.example.friendsandflails.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.friendsandflails.LandingPageActivity;
import com.example.friendsandflails.entities.User;
import com.example.friendsandflails.entities.UserDAO;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FlailRepo {

    private UserDAO userDAO;

    private ArrayList<User> allUsers;

    private static FlailRepo repository;

    private FlailRepo(Application application) {
        FlailDatabase db = FlailDatabase.getDatabase(application);
        this.userDAO = db.userDAO();
    }

    public static FlailRepo getRepository(Application application) {
        if (repository != null) {
            return repository;
        }
        Future<FlailRepo> future = FlailDatabase.databaseWriteExecutor.submit(() -> new FlailRepo(application));
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(LandingPageActivity.TAG, "Problem getting GymLogRepository, thread error");
        }
        return null;
    }

    public void insertUser(User... user) {
        FlailDatabase.databaseWriteExecutor.execute(() -> userDAO.insert(user));
    }

    public LiveData<User> getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    public LiveData<User> getUserByUserId(int loggedInUserId) {
        return userDAO.getUserByUserId(loggedInUserId);
    }
    
}
