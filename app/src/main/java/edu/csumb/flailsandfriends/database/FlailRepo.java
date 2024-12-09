package edu.csumb.flailsandfriends.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import edu.csumb.flailsandfriends.activities.LandingPageActivity;
import edu.csumb.flailsandfriends.entities.BattleRecord;
import edu.csumb.flailsandfriends.entities.BattleRecordDAO;
import edu.csumb.flailsandfriends.entities.Equipment;
import edu.csumb.flailsandfriends.entities.EquipmentDAO;
import edu.csumb.flailsandfriends.entities.User;
import edu.csumb.flailsandfriends.entities.UserDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FlailRepo {

    private UserDAO userDAO;

    private EquipmentDAO equipmentDAO;

    private BattleRecordDAO battleRecordDAO;

    private ArrayList<User> allUsers;

    private static FlailRepo repository;

    private FlailRepo(Application application) {
        FlailDatabase db = FlailDatabase.getDatabase(application);
        this.userDAO = db.userDAO();
        this.equipmentDAO = db.equipmentDAO();
        this.battleRecordDAO = db.battleRecordDAO();
    }

    public static FlailRepo getRepository(Application application) {
        if (repository != null) {
            return repository;
        }
        Future<FlailRepo> future = FlailDatabase.databaseWriteExecutor.submit(() -> new FlailRepo(application));
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(LandingPageActivity.TAG, "Problem getting FlailRepository, thread error");
        }
        return null;
    }

    public void insertUser(User... user) {
        FlailDatabase.databaseWriteExecutor.execute(() -> userDAO.insert(user));
    }

    public LiveData<User> getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public LiveData<User> getUserByUserId(int loggedInUserId) {
        return userDAO.getUserByUserId(loggedInUserId);
    }

    public void insertEquipment(Equipment... equipment) {
        FlailDatabase.databaseWriteExecutor.execute(() -> equipmentDAO.insert(equipment));
    }

    public LiveData<Equipment> getEquipmentByName(String name) {
        return equipmentDAO.getEquipmentByName(name);
    }

    public LiveData<List<Equipment>> getEquipmentById(int equipmentId) {
        return equipmentDAO.getEquipmentById(equipmentId);
    }

    public void insertBattleRecord(BattleRecord... battleRecord) {
        FlailDatabase.databaseWriteExecutor.execute(() -> battleRecordDAO.insert(battleRecord));
    }

    public LiveData<BattleRecord> getBattleRecordByTitle(String title) {
        return battleRecordDAO.getBattleByTitle(title);
    }

    public LiveData<BattleRecord> getBattleById(int battleId) {
        return battleRecordDAO.getBattleById(battleId);
    }
    
}
