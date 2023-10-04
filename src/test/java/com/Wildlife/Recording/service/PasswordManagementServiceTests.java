package com.Wildlife.Recording.service;

import com.Wildlife.Recording.model.entities.PasswordHash;
import com.Wildlife.Recording.model.entities.User;
import com.Wildlife.Recording.model.exceptions.InvalidPasswordException;
import com.Wildlife.Recording.model.exceptions.PasswordHashNotFoundException;
import com.Wildlife.Recording.model.exceptions.UserNotFoundException;
import com.Wildlife.Recording.repository.PasswordHashRepository;
import com.Wildlife.Recording.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.Wildlife.Recording.configuration.DBSeeder.DB_SEEDER_USERS;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PasswordManagementServiceTests {


    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordHashRepository passwordHashRepository;

    @InjectMocks
    private PasswordService passwordService;

    private User user;


    @Test
    public void updatePasswordValid(){
        //Prepare
        user = DB_SEEDER_USERS.get(0);
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(passwordHashRepository.findById(user.getPasswordHash().getId())).willReturn(Optional.of(user.getPasswordHash()));

        //Act
        try {
            passwordService.updatePassword(
                    user.getId(),
                    user.getPasswordHash().getId(),
                    "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
                    "5c29a959abce4eda5f0e7a4e7ea53dce4fa0f0abbe8eaa63717e2fed5f193d32",
                    "salt");
        }catch(Exception exception){
            fail();
        }
    }

    @Test
    public void updatePasswordWithInvalidOldPassword(){
        //Prepare
        user = DB_SEEDER_USERS.get(0);
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(passwordHashRepository.findById(user.getPasswordHash().getId())).willReturn(Optional.of(user.getPasswordHash()));

        //Act & Assert
        try {
            passwordService.updatePassword(
                    user.getId(),
                    user.getPasswordHash().getId(),
                    "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d154218",
                    "11507a0e2f5e69d5dfa40a62a1bd7b6ee57e6bcd85c67c9b8431b36fff21c437",
                    "salt");
        }catch(InvalidPasswordException exception){
            return;
        }
        fail();
    }

    @Test
    public void updatePasswordWithInvalidPasswordHashId(){
        //Prepare
        user = DB_SEEDER_USERS.get(0);
        UUID passwordUuid = UUID.randomUUID();
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(passwordHashRepository.findById(passwordUuid)).willReturn(Optional.empty());

        //Act & Assert
        try{
            passwordService.updatePassword(
                    user.getId(),
                    passwordUuid,
                    user.getPasswordHash().getHashValue(),
                    "5c29a959abce4eda5f0e7a4e7ea53dce4fa0f0abbe8eaa63717e2fed5f193d31",
                    "salt");
        }catch(PasswordHashNotFoundException exception){
            return;
        }
        fail();
    }

    @Test
    public void updatePasswordWithInvalidUserId(){
        //Prepare
        user = DB_SEEDER_USERS.get(0);
        UUID uuid = UUID.randomUUID();
        given(userRepository.findById(uuid)).willReturn(Optional.empty());
        given(passwordHashRepository.findById(user.getPasswordHash().getId())).willReturn(Optional.of(user.getPasswordHash()));

        //Act
        try {
            passwordService.updatePassword(
                    uuid,
                    user.getPasswordHash().getId(),
                    user.getPasswordHash().getHashValue(),
                    "5c29a959abce4eda5f0e7a4e7ea53dce4fa0f0abbe8eaa63717e2fed5f193d31",
                    "salt");
        }catch(UserNotFoundException exception){
            return;
        }
        fail();
    }
}
