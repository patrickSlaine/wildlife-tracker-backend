package com.Wildlife.Recording.service;

import com.Wildlife.Recording.model.entities.PasswordHash;
import com.Wildlife.Recording.model.exceptions.InvalidPasswordException;
import com.Wildlife.Recording.model.exceptions.PasswordHashNotFoundException;
import com.Wildlife.Recording.repository.PasswordHashRepository;
import com.Wildlife.Recording.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordService {

    @Autowired
    private PasswordHashRepository passwordHashRepository;

    public PasswordService(PasswordHashRepository passwordHashRepository){
        this.passwordHashRepository = passwordHashRepository;
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

    public void updatePassword(UUID id, String oldPasswordHash, String newPasswordHash, String salt){
        try{
            PasswordHash passwordHash = passwordHashRepository.findById(id).get();

            if (!passwordHash.getHashValue().equals(oldPasswordHash)){
                throw new InvalidPasswordException("Invalid Password");
            }
            passwordHash.setHashValue(newPasswordHash);
            passwordHash.setSalt(salt);
            passwordHashRepository.save(passwordHash);
        }catch(EntityNotFoundException exception){
            throw new PasswordHashNotFoundException("Password Hash with id " + id + " was not found.");
        }catch(Exception exception){
            throw new RuntimeException("Error processing data");
        }
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
