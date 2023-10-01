package com.Wildlife.Recording.configuration;

import com.Wildlife.Recording.model.entities.PasswordHash;
import com.Wildlife.Recording.model.entities.Recording;
import com.Wildlife.Recording.model.entities.User;
import com.Wildlife.Recording.repository.PasswordHashRepository;
import com.Wildlife.Recording.repository.RecordingRepository;
import com.Wildlife.Recording.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Configuration
@Profile("dbseeder")
public class DBSeeder implements CommandLineRunner {

    public static PasswordHash userOnePassword = new PasswordHash(
            null,
            "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");

    public static final List<Recording> DB_SEEDER_RECORDINGS = List.of(
            new Recording(null,"Cat",1.0000,2.0000,"/folderOne/cat.png", UUID.randomUUID()),
            new Recording(null,"Dog",2.0000,1.0000,"/folderOne/dog.png", UUID.randomUUID()),
            new Recording(null,"Horse",1.5000,2.2000,"/folderOne/horse.png", UUID.randomUUID())
    );

    public static final List<User> DB_SEEDER_USERS = List.of(
           new User(null,
                   "TestUser",
                   "Joe",
                   "Bloggs",
                   new Date("29/03/2001"),
                   new Date("20/10/2023"),
                   userOnePassword)
    );

    public static final List<PasswordHash> DB_SEEDER_PASSWORDS = List.of(
            userOnePassword
    );

    @Autowired
    private RecordingRepository recordingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordHashRepository passwordHashRepository;

    @Override
    public void run(String... args) throws Exception{
        recordingRepository.saveAll(DB_SEEDER_RECORDINGS);
        userRepository.saveAll(DB_SEEDER_USERS);
        passwordHashRepository.saveAll(DB_SEEDER_PASSWORDS);
    }
}
