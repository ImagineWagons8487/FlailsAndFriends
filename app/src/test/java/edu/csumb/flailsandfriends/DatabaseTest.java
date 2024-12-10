package edu.csumb.flailsandfriends;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.csumb.flailsandfriends.database.FlailDatabase;
import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.entities.BattleRecord;
import edu.csumb.flailsandfriends.entities.BattleRecordDAO;
import edu.csumb.flailsandfriends.entities.Equipment;
import edu.csumb.flailsandfriends.entities.EquipmentDAO;
import edu.csumb.flailsandfriends.entities.User;
import edu.csumb.flailsandfriends.entities.UserDAO;

@Config(manifest= Config.NONE)
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private UserDAO userDAO;
    private EquipmentDAO equipmentDAO;
    private BattleRecordDAO battleRecordDAO;
    private FlailRepo repository;
    private FlailDatabase db;

    @Before
    public void createDB() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, FlailDatabase.class)
                .allowMainThreadQueries()  // Allow Room to run queries on the main thread in tests
                .build();
        userDAO = db.userDAO();
        equipmentDAO = db.equipmentDAO();
        battleRecordDAO = db.battleRecordDAO();
        repository = FlailRepo.getRepository((Application) context);
    }

    @After
    public void closeDB() throws IOException {
        db.close();
    }

    @Test
    public void insertUserTest() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                User testUser = new User("test@gmail.com", "testPass");
                userDAO.insert(testUser);
                User retrievedUser = repository.getUserByEmailOffline("test@gmail.com");

                assertThat(retrievedUser.getUsername(), equalTo(testUser.getUsername()));
                assertThat(retrievedUser.getPassword(), equalTo(testUser.getPassword()));
                assertThat(retrievedUser.getId(), equalTo(testUser.getId()));
            }
        });
        executor.shutdown();
    }

    @Test
    public void deleteUserTest() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                User testUser = new User("test@gmail.com", "testPass");
                userDAO.insert(testUser);

                User retrievedUser = repository.getUserByEmailOffline("test@gmail.com");
                assertThat(retrievedUser.getUsername(), equalTo(testUser.getUsername()));

                userDAO.delete(testUser);

                retrievedUser = repository.getUserByEmailOffline("test@gmail.com");
                assertThat(retrievedUser.getUsername(), equalTo(null));
            }
        });
        executor.shutdown();
    }

    @Test
    public void updateUserTest() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                User testUser = new User("test@gmail.com", "testPass");
                userDAO.insert(testUser);

                User retrievedUser = repository.getUserByEmailOffline("test@gmail.com");
                assertThat(retrievedUser.getUsername(), equalTo(testUser.getUsername()));

                retrievedUser.setUsername("newTest@gmail.com");
                retrievedUser.setPassword("newTestPass");
                userDAO.update(retrievedUser);


                retrievedUser = repository.getUserByEmailOffline("newtest@gmail.com");
                assertThat(retrievedUser.getUsername(), equalTo("newTest@gmail.com"));
                assertThat(retrievedUser.getPassword(), equalTo("newTestPass"));
            }
        });
        executor.shutdown();
    }

    @Test
    public void insertEquipmentTest() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                Equipment testEquipment = new Equipment("Testcalibur", "@drawable/testcalibur", 1);
                equipmentDAO.insert(testEquipment);
                Equipment retrievedEquipment = repository.getEquipmentByNameOffline("Testcalibur");

                assertThat(retrievedEquipment.getEquipmentName(), equalTo(testEquipment.getEquipmentName()));
                assertThat(retrievedEquipment.getBitmapName(), equalTo(testEquipment.getBitmapName()));
                assertThat(retrievedEquipment.getBuff(), equalTo(testEquipment.getBuff()));
            }
        });
        executor.shutdown();
    }

    @Test
    public void deleteEquipmentTest() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                Equipment testEquipment = new Equipment("Testcalibur", "@drawable/testcalibur", 1);
                equipmentDAO.insert(testEquipment);
                equipmentDAO.delete(testEquipment);
                Equipment retrievedEquipment = repository.getEquipmentByNameOffline("Testcalibur");

                assertThat(retrievedEquipment.getEquipmentName(), equalTo(null));
                assertThat(retrievedEquipment.getBitmapName(), equalTo(null));
            }
        });
        executor.shutdown();
    }

    @Test
    public void updateEquipmentTest() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                Equipment testEquipment = new Equipment("Testcalibur", "@drawable/testcalibur", 1);
                equipmentDAO.insert(testEquipment);
                Equipment retrievedEquipment = repository.getEquipmentByNameOffline("Testcalibur");
                assertThat(retrievedEquipment.getEquipmentName(), equalTo(testEquipment.getEquipmentName()));
                retrievedEquipment.setEquipmentName("Infinity Gauntlet");
                retrievedEquipment.setBitmapName("@drawable/pander.jpg");
                retrievedEquipment.setBuff(6);
                equipmentDAO.update(retrievedEquipment);
                retrievedEquipment = repository.getEquipmentByNameOffline("Infinity Gauntlet");

                assertThat(retrievedEquipment.getEquipmentName(), equalTo("Infinity Gauntlet"));
                assertThat(testEquipment.getEquipmentName(), equalTo("Testcalibur"));

                assertThat(testEquipment.getBitmapName(), equalTo("@drawable/testcalibur"));
                assertThat(retrievedEquipment.getBitmapName(), equalTo("@drawable/pander.jpg"));

                assertThat(retrievedEquipment.getBuff(), equalTo(1));
                assertThat(retrievedEquipment.getBuff(), equalTo(6));
            }
        });
        executor.shutdown();
    }

    @Test
    public void insertBattleRecordTest() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                BattleRecord testRecord = new BattleRecord(4, "Chewbacca VS Mewtwo EX", "R P S R P P");
                battleRecordDAO.insert(testRecord);
                BattleRecord retrievedRecord = repository.getBattleRecordByTitleOffline("Chewbacca VS Mewtwo EX");

                assertThat(testRecord.getTitle(), equalTo(retrievedRecord.getTitle()));

                assertThat(testRecord.getDate(), equalTo(retrievedRecord.getDate()));
            }
        });
        executor.shutdown();
    }

    @Test
    public void deleteBattleRecordTest() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                BattleRecord testRecord = new BattleRecord(4, "Chewbacca VS Mewtwo EX", "R P S R P P");
                battleRecordDAO.insert(testRecord);
                battleRecordDAO.delete(testRecord);
                BattleRecord retrievedRecord = repository.getBattleRecordByTitleOffline("Chewbacca VS Mewtwo EX");

                assertThat(retrievedRecord.getTitle(), equalTo(null));
            }
        });
        executor.shutdown();
    }
    @Test
    public void updateBattleRecordTest() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                BattleRecord testRecord = new BattleRecord(4, "Chewbacca VS Mewtwo EX", "R P S R P P");
                battleRecordDAO.insert(testRecord);
                BattleRecord retrievedRecord = repository.getBattleRecordByTitleOffline("Chewbacca VS Mewtwo EX");
                retrievedRecord.setDate(99999999);
                retrievedRecord.setTitle("Morrigan VS Felicia");
                battleRecordDAO.update(retrievedRecord);
                retrievedRecord = repository.getBattleRecordByTitleOffline("Morrigan VS Felicia");
                assertThat(retrievedRecord.getTitle(), equalTo("Morrigan VS Felicia"));
                assertThat(retrievedRecord.getDate(), equalTo(99999999));
            }
        });
        executor.shutdown();
    }


}
