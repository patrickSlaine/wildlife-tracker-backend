package com.Wildlife.Recording.controller;

import com.Wildlife.Recording.model.exceptions.PasswordHashNotFoundException;
import com.Wildlife.Recording.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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

//    @PutMapping("/put/{uuid}")
//    public ResponseEntity<?> updatePassword(@PathVariable UUID uuid, @Valid @RequestBody ){
//
//    }

}
