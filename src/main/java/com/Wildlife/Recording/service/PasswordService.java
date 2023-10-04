package com.Wildlife.Recording.service;

import com.Wildlife.Recording.model.entities.PasswordHash;
import com.Wildlife.Recording.model.entities.User;
import com.Wildlife.Recording.model.exceptions.InvalidPasswordException;
import com.Wildlife.Recording.model.exceptions.PasswordHashNotFoundException;
import com.Wildlife.Recording.model.exceptions.UserNotFoundException;
import com.Wildlife.Recording.repository.PasswordHashRepository;
import com.Wildlife.Recording.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordService {

    @Autowired
    private PasswordHashRepository passwordHashRepository;

    @Autowired
    private UserRepository userRepository;

    public PasswordService(PasswordHashRepository passwordHashRepository, UserRepository userRepository){
        this.passwordHashRepository = passwordHashRepository;
        this.userRepository = userRepository;
    }

    public boolean validatePassword(UUID passwordHashId, String hashValue){
        try{
            PasswordHash passwordHash = passwordHashRepository.findById(passwordHashId).get();
            return passwordHash.getHashValue().equals(hashValue);
        }catch(EntityNotFoundException exception){
            throw new PasswordHashNotFoundException("PasswordHash with Id " + passwordHashId + " was not found.");
        }catch(Exception exception){
            throw new RuntimeException("Error processing data");
        }
    }

    public void updatePassword(UUID userUuid, UUID passwordUuid ,String oldPasswordHash, String newPasswordHash, String salt){
            Optional<User> userData = userRepository.findById(userUuid);
            Optional<PasswordHash> passwordHashOptional = passwordHashRepository.findById(passwordUuid);

            if(userData.isEmpty()){
                throw new UserNotFoundException("User with id " + userUuid + " was not found.");
            }
            if(passwordHashOptional.isEmpty()){
                throw new PasswordHashNotFoundException("Password Hash with id " + passwordUuid + " was not found.");
            }

            PasswordHash password = passwordHashOptional.get();
            if(!password.getHashValue().equals(oldPasswordHash)){
                throw new InvalidPasswordException("Invalid Password");
            }

            password.setHashValue(newPasswordHash);
            password.setSalt(salt);
            passwordHashRepository.save(password);
    }

    public String retrieveSalt(UUID id){
        try{
            PasswordHash passwordHash = passwordHashRepository.findById(id).get();
            return passwordHash.getSalt();
        }catch(EntityNotFoundException exception){
            throw new PasswordHashNotFoundException("Password Hash with id " + id + " was not found.");
        }catch(Exception exception){
            throw new RuntimeException("Error processing data");
        }
    }



}
