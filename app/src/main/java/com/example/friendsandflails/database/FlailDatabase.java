package edu.csumb.flailsandfriends.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import edu.csumb.flailsandfriends.activities.LandingPageActivity;
import edu.csumb.flailsandfriends.entities.BattleRecord;
import edu.csumb.flailsandfriends.entities.BattleRecordDAO;
import edu.csumb.flailsandfriends.entities.Equipment;
import edu.csumb.flailsandfriends.entities.EquipmentDAO;
import edu.csumb.flailsandfriends.entities.User;
import edu.csumb.flailsandfriends.entities.UserDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Equipment.class, BattleRecord.class}, version = 3, exportSchema = false)
public abstract class FlailDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "FlailDatabase";

    public static volatile FlailDatabase INSTANCE;

    public static final String USER_TABLE = "userTable";

    public static final String EQUIPMENT_TABLE = "equipmentTable";

    public static final String RECORD_TABLE = "recordTable";

    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract UserDAO userDAO();

    public abstract EquipmentDAO equipmentDAO();

    public abstract BattleRecordDAO battleRecordDAO();

    static FlailDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FlailDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    FlailDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i(LandingPageActivity.TAG, "DATABASE CREATED!");

            databaseWriteExecutor.execute(() -> {
                UserDAO userDao = INSTANCE.userDAO();
                userDao.deleteAll();

                User admin = new User("admin2", "admin2");

                User testUser1 = new User("testuser1", "testuser1");

                EquipmentDAO equipmentDAO = INSTANCE.equipmentDAO();
                equipmentDAO.deleteAll();

                Equipment shrtSword = new Equipment("Short Sword", "@drawable/light", 1);
                equipmentDAO.insert(shrtSword);

                Equipment shield = new Equipment("Shield", "@drawable/heavy", 1);
                equipmentDAO.insert(shield);

                admin.addToInventory(shrtSword.getId());
                admin.addToInventory(shield.getId());
                admin.setAdmin(true);
                userDao.insert(admin);

                testUser1.addToInventory(shrtSword.getId());
                testUser1.addToInventory(shield.getId());
                userDao.insert(testUser1);
            });
        }
    };
}
