package edu.csumb.flailsandfriends;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.csumb.flailsandfriends.entities.User;

public class UserEntityUnitTest {
    private String name = "Bob", password = "bobPassword";
    private User user = new User(name, password);

    @Test
    public void userNameTest(){
        String newName = "Bobbo";
        assertEquals(user.getUsername(), name);
        user.setUsername(newName);
        assertEquals(user.getUsername(), newName);
    }

    @Test
    public void userPasswordTest(){
        String newPass = "pass";
        assertEquals(user.getPassword(), password);
        user.setPassword(newPass);
        assertEquals(user.getPassword(), newPass);
    }
}
