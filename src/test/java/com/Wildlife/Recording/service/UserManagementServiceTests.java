package com.Wildlife.Recording.service;

import com.Wildlife.Recording.model.entities.PasswordHash;
import com.Wildlife.Recording.model.entities.User;
import com.Wildlife.Recording.model.exceptions.InvalidPasswordException;
import com.Wildlife.Recording.model.exceptions.UserCreationException;
import com.Wildlife.Recording.model.exceptions.UserNotFoundException;
import com.Wildlife.Recording.repository.PasswordHashRepository;
import com.Wildlife.Recording.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserManagementServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordHashRepository passwordHashRepository;

    @InjectMocks
    private UserManagementService userManagementService;

    private User user;

    @BeforeEach
    public void setup(){
        user = new User(
                null,
                "TestUser1",
                "Joe",
                "Bloggs",
                new Date("29/09/2001"),
                new Date("13/09/2020"),
                new PasswordHash(
                        null,
                        "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
                        "salt")
                );
    }

    @Test
    public void createUserValid(){
        //Prepare
        given(userRepository.findByUserName(user.getUserName())).willReturn(null);
        given(userRepository.existsById(user.getId())).willReturn(true);

        //Act
        boolean added = userManagementService.createUser(user);

        //Assert
        assertTrue(added);
    }

    @Test
    public void createUserWithUserNameThatAlreadyExists(){
        //Prepare
        given(userRepository.findByUserName(user.getUserName())).willReturn(user);
        //Act
        try{
            userManagementService.createUser(user);
        }catch(UserCreationException exception){
            return;
        }
        fail();
    }

    @Test
    public void readUserByIdValid(){
        //Prepare
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //Act
        User testData = userManagementService.readUser(user.getId());

        //Assert
        assertEquals(user.hashCode(),testData.hashCode());
    }

    @Test
    public void readUserByIdWithInvalidId(){
        //Prepare
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        //Act & Assert
        try{
            userManagementService.readUser(user.getId());
        }catch(UserNotFoundException exception){
            return;
        }
        fail();
    }

    @Test
    public void readUsersValid(){
        //Prepare
        List<User> users = List.of(
                new User(
                    null,
                    "TestUser1",
                    "Joe",
                    "Bloggs",
                    new Date("29/09/2001"),
                    new Date("13/09/2020"),
                    new PasswordHash(
                        null,
                        "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
                            "salt")),
                new User(
                        null,
                        "TestUser2",
                        "Jane",
                        "Bloggs",
                        new Date("30/09/2002"),
                        new Date("14/10/2022"),
                        new PasswordHash(
                                null,
                                "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
                                "salt")));


        given(userRepository.findAll()).willReturn(users);

        //Act
        List<User> testData = userManagementService.readUsers();

        //Assert
        assertEquals(users.hashCode(),testData.hashCode());
    }

    @Test
    public void updateUserValid(){
        //Prepare
        User newUser = new User(
                user.getId(),
                "TestUpdate",
                "NewUser",
                "UserSurname",
                new Date("20/10/2003"),
                new Date("20/10/2022"),
                user.getPasswordHash()
        );
        given(userRepository.existsById(user.getId())).willReturn(true);
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(newUser);
        //Act
        User testResult = userManagementService.updateUser(
                user.getId(),
                newUser);

        //Assert
        assertEquals(newUser.hashCode(),testResult.hashCode());
    }

    @Test
    public void updateUserInvalidUuid(){
        //Prepare
        UUID uuid = UUID.randomUUID();
        given(userRepository.existsById(uuid)).willReturn(false);

        //Act & Assert
        try{
            userManagementService.updateUser(uuid,new User());
        }catch(UserNotFoundException exception){
            return;
        }
        fail();
    }

    @Test
    public void deleteUserValid(){
        //Prepare
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //Act & Assert
        try{
            userManagementService.deleteUser(user.getId());
        }catch(UserNotFoundException exception){
            fail();
        }catch(Exception exception){
            fail(exception);
        }
        return;
    }

    @Test
    public void deleteUserInvalid(){
        //Prepare
        UUID uuid = UUID.randomUUID();
        given(userRepository.findById(uuid)).willReturn(Optional.ofNullable(null));

        //Act & Assert
        try{
            userManagementService.deleteUser(uuid);
        }catch(UserNotFoundException exception){
            return;
        }catch(Exception exception){
            fail(exception);
        }
        fail();
    }

    @Test
    public void updatePasswordValid(){
        //Prepare
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(passwordHashRepository.findById(user.getPasswordHash().getId())).willReturn(Optional.of(user.getPasswordHash()));

        //Act
        boolean results = userManagementService.updatePassword(
                user.getId(),
                user.getPasswordHash().getId(),
                new PasswordHash(
                        null,
                        "5c29a959abce4eda5f0e7a4e7ea53dce4fa0f0abbe8eaa63717e2fed5f193d31",
                        "salt"
                        ),
                user.getPasswordHash().getHashValue()
        );

        //Assert
        assertTrue(results);
    }

    @Test
    public void updatePasswordWithInvalidOldPassword(){
        //Prepare
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(passwordHashRepository.findById(user.getPasswordHash().getId())).willReturn(Optional.of(user.getPasswordHash()));

        //Act & Assert
        try {
            userManagementService.updatePassword(
                    user.getId(),
                    user.getPasswordHash().getId(),
                    new PasswordHash(
                            null,
                            "11507a0e2f5e69d5dfa40a62a1bd7b6ee57e6bcd85c67c9b8431b36fff21c437",
                            "salt"
                    ),
                    "11507a0e2f5e69d5dfa40a62a1bd7b6ee57e6bcd85c67c9b8431b36fff21c437"
            );
        }catch(InvalidPasswordException exception){
            return;
        }
        fail();
    }

    @Test
    public void updatePasswordWithInvalidPasswordHashId(){
        //Prepare
        UUID passwordUuid = UUID.randomUUID();
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(passwordHashRepository.findById(user.getPasswordHash().getId())).willReturn(Optional.of(
                new PasswordHash(passwordUuid,
                        "5c29a959abce4eda5f0e7a4e7ea53dce4fa0f0abbe8eaa63717e2fed5f193d31"
                        ,"salt")));

        //Act
        boolean results = userManagementService.updatePassword(
                user.getId(),
                user.getPasswordHash().getId(),
                new PasswordHash(
                        null,
                        "5c29a959abce4eda5f0e7a4e7ea53dce4fa0f0abbe8eaa63717e2fed5f193d31",
                        "salt"
                ),
                user.getPasswordHash().getHashValue()
        );

        //Assert
        assertFalse(results);
    }

    @Test
    public void updatePasswordWithInvalidUserId(){
        //Prepare
        UUID uuid = UUID.randomUUID();
        given(userRepository.findById(uuid)).willReturn(Optional.empty());
        given(passwordHashRepository.findById(user.getPasswordHash().getId())).willReturn(Optional.of(user.getPasswordHash()));

        //Act
        boolean results = userManagementService.updatePassword(
                uuid,
                user.getPasswordHash().getId(),
                new PasswordHash(
                        null,
                        "5c29a959abce4eda5f0e7a4e7ea53dce4fa0f0abbe8eaa63717e2fed5f193d31",
                        "salt"
                ),
                user.getPasswordHash().getHashValue()
        );

        //Assert
        assertFalse(results);
    }




}
