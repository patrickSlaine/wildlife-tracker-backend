package com.Wildlife.Recording.repository;

import com.Wildlife.Recording.model.entities.PasswordHash;
import com.Wildlife.Recording.model.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.Wildlife.Recording.configuration.DBSeeder.DB_SEEDER_USERS;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {"spring.jpa.hibernate.ddl-auto=update"})
@ActiveProfiles({"h2","dbseeder"})
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordHashRepository passwordHashRepository;

    @Test
    public void creationOfUserValid(){
        //Prepare
        User user = new User(null,
                "TestUser1",
                "Joe",
                "Bloggs",
                new Date("17/03/2020"),
                new Date("20/05/2023"),
                new PasswordHash(
                        null,
                        "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
                        "salt")
                        );
        //Act
        userRepository.save(user);

        //Test
        Optional<User> testData = userRepository.findById(user.getId());
        Optional<PasswordHash> passwordHashData = passwordHashRepository.findById(user.getPasswordHash().getId());
        assertTrue(testData.isPresent());
        assertTrue(passwordHashData.isPresent());
        assertEquals(testData.get().getUserName(),user.getUserName());
        assertEquals(passwordHashData.get().getHashValue(),user.getPasswordHash().getHashValue());
    }

    @Test
    public void readUserValid(){
        //Prepare
        User user = new User(null,
                "TestUser1",
                "Joe",
                "Bloggs",
                new Date("17/03/2020"),
                new Date("20/05/2023"),
                new PasswordHash(
                        null,
                        "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
                        "salt")
        );
        userRepository.save(user);

        //Act
        Optional<User> testData = userRepository.findById(user.getId());

        //Assert
        assertTrue(testData.isPresent());
        assertEquals(testData.hashCode(), user.hashCode());
    }

    @Test
    @DirtiesContext
    public void readUserInvalid(){
        //Act
        Optional<User> testData = userRepository.findById(UUID.randomUUID());

        //Assert
        assertFalse(testData.isPresent());
    }

    @Test
    @DirtiesContext
    public void readUserByUserName(){
        //Prepare
        User user = new User(null,
                "TestUser1",
                "Joe",
                "Bloggs",
                new Date("17/03/2020"),
                new Date("20/05/2023"),
                new PasswordHash(
                        null,
                        "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
                        "salt")
        );
        userRepository.save(user);

        //Act
        User testData = userRepository.findByUserName(user.getUserName());

        //Assert
        assertNotNull(testData);
        assertEquals(testData.getId(), user.getId());
    }

    @Test
    @DirtiesContext
    public void readUserByUserNameInvalid(){
        //Act
        User testData = userRepository.findByUserName("InvalidUserName");

        //Assert
        assertNull(testData);
    }

}
