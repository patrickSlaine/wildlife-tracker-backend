package com.Wildlife.Recording.controller;

import com.Wildlife.Recording.model.entities.Recording;
import com.Wildlife.Recording.model.exceptions.RecordingNotFoundException;
import com.Wildlife.Recording.service.RecordingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/recording")
public class RecordingController {

    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private RecordingService recordingService;

    @GetMapping("/get/{uuid}")
    public Recording getRecording(@PathVariable UUID uuid){
        try{
            logger.info("Received HTTP GET",uuid);
            return recordingService.readRecording(uuid);
        }catch(RecordingNotFoundException exception){
            logger.warn("User Error", exception.getMessage());
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }catch(Exception exception){
            logger.error("Internal Error",exception.getMessage());
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR,exception.getMessage());
        }
    }

    @GetMapping("/get")
    public List<Recording> getRecordings(){
        logger.info("Received HTTP GET");
        return recordingService.readAllRecording();
    }

    @PostMapping("/post")
    public ResponseEntity<?> postRecording(@Valid @RequestBody Recording recording){
        logger.info("Received HTTP POST",recording);
        boolean added = recordingService.createRecording(recording);
        if(added){
            return ResponseEntity.ok("Recording Added");
        }else{
            logger.error("Internal Error",recording);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/put/{uuid}")
    public ResponseEntity<?> putRecording(@PathVariable UUID uuid, @Valid @RequestBody Recording recording){
        try{
            logger.info("Received HTTP PUT",uuid,recording);
            return ResponseEntity.ok(recordingService.updateRecording(uuid,recording));
        }catch(RecordingNotFoundException exception){
            logger.warn("User Error",exception.getMessage());
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }catch(Exception exception){
            logger.error("Internal Error",exception.getMessage());
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<?> deleteRecording(@PathVariable UUID uuid){
        try{
            logger.info("Received HTTP DELETE", uuid);
            recordingService.deleteRecording(uuid);
            return ResponseEntity.ok().build();
        }catch(RecordingNotFoundException exception){
            logger.warn("User Error",exception.getMessage());
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }catch(Exception exception){
            logger.error("Internal Error",exception.getMessage());
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }
}
