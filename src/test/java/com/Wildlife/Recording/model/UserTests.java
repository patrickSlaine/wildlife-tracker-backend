package com.Wildlife.Recording.model;

import com.Wildlife.Recording.model.entities.PasswordHash;
import com.Wildlife.Recording.model.entities.User;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    private static String testUserName = "TestUser";
    private static String testFirstName = "Joe";
    private static String testLastName = "Bloggs";
    private static Date dob = new Date("10/10/2002");
    private static Date joinDate = new Date("10/11/2022");
    private static PasswordHash passwordHash = new PasswordHash(
            null,
            "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
            "salt");
    private User user;
    @BeforeEach
    private void beforeEach(){
        user = new User(
                null,
                testUserName,
                testFirstName,
                testLastName,
                dob,
                joinDate,
                passwordHash);
    }

    @Test
    public void getterTesting(){
        assertEquals(testUserName, user.getUserName());
        assertEquals(testFirstName,user.getFirstName());
        assertEquals(testLastName,user.getLastName());
        assertEquals(dob, user.getDateOfBirth());
        assertEquals(joinDate, user.getJoiningDate());
        assertEquals(passwordHash,user.getPasswordHash());
    }

    @Test
    public void setterTesting(){

    }
}
