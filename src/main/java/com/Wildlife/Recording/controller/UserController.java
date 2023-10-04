package com.Wildlife.Recording.controller;

import com.Wildlife.Recording.controller.valueObjects.PasswordUpdate;
import com.Wildlife.Recording.model.entities.PasswordHash;
import com.Wildlife.Recording.model.entities.User;
import com.Wildlife.Recording.model.exceptions.UserCreationException;
import com.Wildlife.Recording.model.exceptions.UserNotFoundException;
import com.Wildlife.Recording.service.UserManagementService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserManagementService userManagementService;

    @GetMapping("/get/{uuid}")
    public User getUser(@PathVariable UUID uuid){
        try{
            logger.info("Received HTTP GET", uuid);
            return userManagementService.readUser(uuid);
        }catch(UserNotFoundException exception){
            logger.warn("User Error",exception.getMessage());
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }catch(Exception exception){
            logger.error("Internal Error", exception.getMessage());
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Error occurred while processing request");
        }
    }

    @GetMapping("/get")
    public List<User> getUsers(){
        try{
            logger.info("Received HTTP GET");
            return userManagementService.readUsers();
        }catch(Exception exception){
            logger.warn("Internal Error", exception.getMessage());
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Error occurred while processing request");
        }
    }

    @PostMapping("/post")
    public ResponseEntity<?> postUser(@Valid @RequestBody User user){
        try{
            logger.info("Received HTTP POST", user);
            boolean created = userManagementService.createUser(user);

            if(created){
                logger.info("HTTP POST Response 200",user);
                return ResponseEntity.ok("User Created");
            }
            throw new Exception();
        }catch(UserCreationException exception){
            logger.warn("User Error", exception.getMessage());
            throw new ResponseStatusException(BAD_REQUEST, exception.getMessage());
        }catch(Exception exception){
            logger.error("Internal Error",exception.getMessage());
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Error occurred while processing request");
        }
    }

    @PutMapping("/put/{uuid}")
    public ResponseEntity<?> putUser(@PathVariable UUID uuid, @Valid @RequestBody User user){
        try{
            logger.info("Received HTTP PUT",uuid,user);
            return ResponseEntity.ok(userManagementService.updateUser(uuid,user));
        }catch(UserNotFoundException exception){
            logger.warn("User Error",uuid,user);
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }catch(Exception exception){
            logger.error("Internal Error",exception.getMessage());
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Error occurred while processing request");
        }
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID uuid){
        try{
            logger.info("Received HTTP DELETE", uuid);
            userManagementService.deleteUser(uuid);
            return ResponseEntity.ok("User Deleted");
        }catch(UserNotFoundException exception){
            logger.warn("User Error",uuid);
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }catch(Exception exception){
            logger.error("Internal Error",exception.getMessage());
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Error occurred while processing request");
        }
    }

}
