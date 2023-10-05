package com.Wildlife.Recording.controller;

import com.Wildlife.Recording.controller.valueObjects.PasswordUpdate;
import com.Wildlife.Recording.controller.valueObjects.PasswordValidate;
import com.Wildlife.Recording.model.entities.PasswordHash;
import com.Wildlife.Recording.model.exceptions.InvalidPasswordException;
import com.Wildlife.Recording.model.exceptions.PasswordHashNotFoundException;
import com.Wildlife.Recording.model.exceptions.UserNotFoundException;
import com.Wildlife.Recording.service.PasswordService;
import com.Wildlife.Recording.service.UserManagementService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/password")
public class PasswordController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private PasswordService passwordService;

    @GetMapping("/salt/{uuid}")
    public ResponseEntity<String> getSalt(@PathVariable UUID uuid){
        try{
            logger.info("Received HTTP GET",uuid);
            String salt = passwordService.retrieveSalt(uuid);
            return ResponseEntity.ok(salt);
        }catch(PasswordHashNotFoundException exception){
            logger.warn("User Error", exception.getMessage());
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }catch(Exception exception){
            logger.error("Internal Error", exception.getMessage());
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Error while processing your request.");
        }
    }

    @PutMapping("/updatePassword/{userUuid}&{passwordUuid}")
    public ResponseEntity<?> updatePassword(@PathVariable("userUuid") UUID userUuid, @PathVariable("passwordUuid") UUID passwordUuid, @Valid @RequestBody PasswordUpdate passwordData){
        try{
            logger.info("Received HTTP PUT", userUuid,passwordUuid,passwordData);
            passwordService.updatePassword(userUuid,passwordUuid,passwordData.oldPasswordHash, passwordData.newPasswordHash, passwordData.newSalt);
            return ResponseEntity.ok("User Created");
        }catch(UserNotFoundException exception){
            logger.warn("User Error",exception.getMessage());
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }catch(PasswordHashNotFoundException exception){
            logger.warn("User Error",exception.getMessage());
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }catch(InvalidPasswordException exception){
            logger.warn("User Error",exception.getMessage());
            throw new ResponseStatusException(BAD_REQUEST, exception.getMessage());
        }
        catch(Exception exception){
            logger.error("Internal Error",exception.getMessage());
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR,exception.getMessage());
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validatePassword(@Valid @RequestBody PasswordValidate passwordValidate){
        try{
            logger.info("Received HTTP GET",passwordValidate);
            boolean validated = passwordService.validatePassword(
                                                passwordValidate.passwordHashUuid,
                                                passwordValidate.hashValue);
            if(validated){
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        }catch(PasswordHashNotFoundException exception){
            logger.warn("User Error",exception.getMessage());
            throw new ResponseStatusException(NOT_FOUND,exception.getMessage());
        }catch(Exception exception){
            logger.error("Internal Error", exception.getMessage());
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

}
