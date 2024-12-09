package edu.csumb.flailsandfriends;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.csumb.flailsandfriends.database.FlailRepo;
import edu.csumb.flailsandfriends.entities.BattleRecord;
import edu.csumb.flailsandfriends.entities.BattleRecordDAO;
import edu.csumb.flailsandfriends.entities.Equipment;
import edu.csumb.flailsandfriends.entities.EquipmentDAO;
import edu.csumb.flailsandfriends.entities.User;
import edu.csumb.flailsandfriends.entities.UserDAO;

/**
 * These database tests do not interact directly with our database, instead they
 * interact with mock DAO's made using Mockito. This allows me to test without the
 * application running and without filling the database with junk.
 *
 * However, because databaseWriteExecutor is a static field that never gets shut down,
 * these test will hang if run as a group. By that I mean, they will run, pass, but then
 * it seems the executor stalls for some reason I could not quite pinpoint. While the process
 * can be ended effectively manually, I am sure there is a better way of doing this.
 *
 * I could edit the database to shut down for these tests, thereby removing the issue
 * but I worry that will break things elsewhere. Run individually, each test will pass
 * and not hang. I spent hours finding a work around and could not find a satisfactory one.
 *
 * Hopefully, what I have here will suffice.
 * **/

public class DatabaseTest {
    /**
     * These are all mock DAO's made using Mockito
     * **/
    @Mock
    private UserDAO mockUserDao;

    @Mock
    private EquipmentDAO mockEquipmentDao;

    @Mock
    private BattleRecordDAO mockBattleRecordDao;

    private FlailRepo repository;

    /**
     * Sets up Mockito and makes a repo using the @Mock
     * versions of the DAO's
     * **/
    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        repository = new FlailRepo(mockUserDao, mockEquipmentDao, mockBattleRecordDao);
    }

    /**
     * Makes a new User object and inserts it using the @Mock DAO
     * then uses Mockito's public {@code verify} function to ensure
     * that the {@code insert()} in UserDAO.java was called with the correct user.
     * **/
    @Test
    public void insertUserMockTest() {
        User testUser = new User("test@gmail.com","testPass");

        repository.insertUser(testUser);

        try {
            Thread.sleep(100);
            verify(mockUserDao).insert(testUser);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes a new User object and 'deletes' it using the @Mock DAO.
     * Mockito's public {@code verify} function to checks that
     * {@code delete()} in UserDAO.java was called with the right user.
     * **/
    @Test
    public void deleteUserMockTest() {
        User testUser = new User("test@gmail.com","testPass");

        repository.deleteUser(testUser);

        try {
            Thread.sleep(100);
            verify(mockUserDao).delete(testUser);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes a new User object and 'updates' it using the @Mock DAO.
     * Mockito's public {@code verify} function to checks that
     * {@code update()} in UserDAO.java was called with the right user.
     * **/
    @Test
    public void updateUserMockTest() {
        User testUser = new User("test@gmail.com","testPass");

        repository.updateUser(testUser);

        try {
            Thread.sleep(100);
            verify(mockUserDao).update(testUser);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes a new Equipment object and inserts it using the @Mock DAO
     * then uses Mockito's public {@code verify} function to ensure
     * that the {@code insert()} in EquipmentDAO.java was called with the correct equipment.
     * **/
    @Test
    public void insertEquipmentMockTest() {
        Equipment testEquipment = new Equipment("Testcalibur","@drawables/testIcon",3);

        repository.insertEquipment(testEquipment);

        try {
            Thread.sleep(100);
            verify(mockEquipmentDao).insert(testEquipment);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes a new Equipment object and 'deletes' it using the @Mock DAO.
     * Mockito's public {@code verify} function to check that
     * {@code delete()} in EquipmentDAO.java was called with the correct equipment.
     * **/
    @Test
    public void deleteEquipmentMockTest() {
        Equipment testEquipment = new Equipment("Testcalibur","@drawables/testIcon",3);

        repository.deleteEquipment(testEquipment);

        try {
            Thread.sleep(100);
            verify(mockEquipmentDao).delete(testEquipment);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes a new Equipment object and 'updates' it using the @Mock DAO.
     * Mockito's public {@code verify} function to check that
     * {@code update()} in EquipmentDAO.java was called with the correct equipment.
     * **/
    @Test
    public void updateEquipmentMockTest() {
        Equipment testEquipment = new Equipment("Testcalibur","@drawables/testIcon",3);

        repository.updateEquipment(testEquipment);

        try {
            Thread.sleep(100);
            verify(mockEquipmentDao).update(testEquipment);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}