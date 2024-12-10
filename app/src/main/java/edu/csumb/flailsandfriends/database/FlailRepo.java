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

    public FlailRepo(Application application) {
        FlailDatabase db = FlailDatabase.getDatabase(application);
        this.userDAO = db.userDAO();
        this.equipmentDAO = db.equipmentDAO();
        this.battleRecordDAO = db.battleRecordDAO();
    }

    public FlailRepo(UserDAO mock, EquipmentDAO mock1, BattleRecordDAO mock2) {
        this.userDAO = mock;
        this.equipmentDAO = mock1;
        this.battleRecordDAO = mock2;
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
    public void updateUser(User... user) {
        FlailDatabase.databaseWriteExecutor.execute(() -> userDAO.update(user));
    }
    public void deleteUser(User... user) {
        FlailDatabase.databaseWriteExecutor.execute(() -> userDAO.delete(user));
    }

    public LiveData<User> getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public User getUserByEmailOffline(String email) {
        return userDAO.getUserByEmailOffline(email);
    }

    public LiveData<User> getUserByUserId(int loggedInUserId) {
        return userDAO.getUserByUserId(loggedInUserId);
    }

    public void insertEquipment(Equipment... equipment) {
        FlailDatabase.databaseWriteExecutor.execute(() -> equipmentDAO.insert(equipment));
    }

    public void deleteEquipment(Equipment... equipment){
        FlailDatabase.databaseWriteExecutor.execute(() -> equipmentDAO.delete(equipment));
    }

    public void updateEquipment(Equipment... equipment){
        FlailDatabase.databaseWriteExecutor.execute(() -> equipmentDAO.update(equipment));
    }

    public LiveData<Equipment> getEquipmentByName(String name) {
        return equipmentDAO.getEquipmentByName(name);
    }

    public Equipment getEquipmentByNameOffline(String name) {
        return equipmentDAO.getEquipmentByNameOffline(name);
    }

    public LiveData<List<Equipment>> getEquipmentById(int equipmentId) {
        return equipmentDAO.getEquipmentById(equipmentId);
    }

    public void insertBattleRecord(BattleRecord... battleRecord) {
        FlailDatabase.databaseWriteExecutor.execute(() -> battleRecordDAO.insert(battleRecord));
    }

    public void deleteBattleRecord(BattleRecord... battleRecord){
        FlailDatabase.databaseWriteExecutor.execute(() -> battleRecordDAO.delete(battleRecord));
    }

    public void updateBattleRecord(BattleRecord... battleRecord){
        FlailDatabase.databaseWriteExecutor.execute(() -> battleRecordDAO.update(battleRecord));
    }

    public LiveData<BattleRecord> getBattleRecordByTitle(String title) {
        return battleRecordDAO.getBattleByTitle(title);
    }

    public BattleRecord getBattleRecordByTitleOffline(String title) {
        return battleRecordDAO.getBattleByTitleOffline(title);
    }

    public LiveData<BattleRecord> getBattleById(int battleId) {
        return battleRecordDAO.getBattleById(battleId);
    }
    
}
