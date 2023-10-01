package com.Wildlife.Recording.service;

import com.Wildlife.Recording.model.entities.PasswordHash;
import com.Wildlife.Recording.model.entities.User;
import com.Wildlife.Recording.model.exceptions.InvalidPasswordException;
import com.Wildlife.Recording.model.exceptions.UserCreationException;
import com.Wildlife.Recording.model.exceptions.UserNotFoundException;
import com.Wildlife.Recording.repository.PasswordHashRepository;
import com.Wildlife.Recording.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordHashRepository passwordHashRepository;

    public UserManagementService(UserRepository userRepository,PasswordHashRepository passwordHashRepository)
    {
        this.userRepository = userRepository;
        this.passwordHashRepository = passwordHashRepository;
    }


    public boolean createUser(User user){
        if(userRepository.findByUserName(user.getUserName()) == null){
            throw new UserCreationException("User with the Username " + user.getUserName()+ " already exists.");
        }
        userRepository.save(user);
        return userRepository.existsById(user.getId());
    }

    public User readUser(UUID uuid){
        Optional<User> user = userRepository.findById(uuid);

        if(user.isPresent()){
            return user.get();
        }
        else{
            throw new UserNotFoundException("User with UUID " + uuid + " was not found.");
        }
    }

    public List<User> readUsers(){
        return userRepository.findAll();
    }

    public User updateUser(UUID uuid, User user){
        if(!userRepository.existsById(uuid)){
            throw new UserNotFoundException("User with UUID " + uuid + " was not found.");
        }
        else{
            User u = userRepository.findById(uuid).get();
            u.setUserName(user.getUserName());
            u.setFirstName(user.getFirstName());
            u.setLastName(user.getLastName());
            u.setDateOfBirth(user.getDateOfBirth());
            return userRepository.save(u);
        }
    }

    public void deleteUser(UUID uuid){
        Optional<User> user = userRepository.findById(uuid);

        if(user.isPresent()){
            userRepository.deleteById(uuid);
        }
        else{
            throw new UserNotFoundException("User with UUID " + uuid + " with not found.");
        }
    }

    public boolean updatePassword(UUID userUuid, UUID passwordUuid ,PasswordHash passwordHash, String oldPasswordHash){
        Optional<User> userData = userRepository.findById(userUuid);
        Optional<PasswordHash> password = passwordHashRepository.findById(passwordUuid);

        if(userData.isPresent()
                && userData.get().getPasswordHash().getId() == password.get().getId()){

            User u = userData.get();
            if(u.getPasswordHash().getHashValue().equals(oldPasswordHash)){
                u.setPasswordHash(passwordHash);
                userRepository.save(u);
                return true;
            }
            throw new InvalidPasswordException("The password you entered is incorrect");
        }
        return false;
    }

}
