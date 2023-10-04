package com.Wildlife.Recording.controller;

import com.Wildlife.Recording.controller.valueObjects.PasswordUpdate;
import com.Wildlife.Recording.model.entities.PasswordHash;
import com.Wildlife.Recording.model.entities.User;
import com.Wildlife.Recording.model.exceptions.UserCreationException;
import com.Wildlife.Recording.model.exceptions.UserNotFoundException;
import com.Wildlife.Recording.service.UserManagementService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
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
    @Autowired
    private UserManagementService userManagementService;

    @GetMapping("/get/{uuid}")
    public User getUser(@PathVariable UUID uuid){
        try{
            return userManagementService.readUser(uuid);
        }catch(UserNotFoundException exception){
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }catch(Exception exception){
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    @GetMapping("/get")
    public List<User> getUsers(){
        try{
            return userManagementService.readUsers();
        }catch(Exception exception){
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    @PostMapping("/post")
    public ResponseEntity<?> postUser(@Valid @RequestBody User user){
        try{
            boolean created = userManagementService.createUser(user);

            if(created){
                return ResponseEntity.ok("User Created");
            }
            throw new Exception();
        }catch(UserCreationException exception){
            throw new ResponseStatusException(BAD_REQUEST, exception.getMessage());
        }catch(Exception exception){
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    @PutMapping("/put/{uuid}")
    public ResponseEntity<?> putUser(@PathVariable UUID uuid, @Valid @RequestBody User user){
        try{
            return ResponseEntity.ok(userManagementService.updateUser(uuid,user));
        }catch(UserNotFoundException exception){
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }catch(Exception exception){
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID uuid){
        try{
            userManagementService.deleteUser(uuid);
            return ResponseEntity.ok("User Deleted");
        }catch(UserNotFoundException exception){
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }catch(Exception exception){
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

}
