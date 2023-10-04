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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/password")
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    @GetMapping("/salt/{uuid}")
    public ResponseEntity<String> getSalt(@PathVariable UUID uuid){
        try{
            String salt = passwordService.retrieveSalt(uuid);
            return ResponseEntity.ok(salt);
        }catch(PasswordHashNotFoundException exception){
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }catch(Exception exception){
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    @PutMapping("/updatePassword/{userUuid}&{passwordUuid}")
    public ResponseEntity<?> updatePassword(@PathVariable("userUuid") UUID userUuid, @PathVariable("passwordUuid") UUID passwordUuid, @Valid @RequestBody PasswordUpdate passwordData){
        try{
            passwordService.updatePassword(userUuid,passwordUuid,passwordData.oldPasswordHash, passwordData.newPasswordHash, passwordData.newSalt);
            return ResponseEntity.ok("User Created");
        }catch(UserNotFoundException exception){
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }catch(PasswordHashNotFoundException exception){
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }catch(InvalidPasswordException exception){
            throw new ResponseStatusException(BAD_REQUEST, exception.getMessage());
        }
        catch(Exception exception){
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR,exception.getMessage());
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validatePassword(@Valid @RequestBody PasswordValidate passwordValidate){
        try{
            boolean validated = passwordService.validatePassword(
                                                passwordValidate.passwordHashUuid,
                                                passwordValidate.hashValue);
            if(validated){
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        }catch(PasswordHashNotFoundException exception){
            throw new ResponseStatusException(NOT_FOUND,exception.getMessage());
        }catch(Exception exception){
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

}
