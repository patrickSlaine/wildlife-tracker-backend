package com.Wildlife.Recording.controller;

import com.Wildlife.Recording.model.entities.Recording;
import com.Wildlife.Recording.model.exceptions.RecordingNotFoundException;
import com.Wildlife.Recording.service.RecordingService;
import jakarta.validation.Valid;
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

    @Autowired
    private RecordingService recordingService;

    @GetMapping("/get/{uuid}")
    public Recording getRecording(@PathVariable UUID uuid){
        try{
            return recordingService.readRecording(uuid);
        }catch(Exception exception){
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }
    }

    @GetMapping("/get")
    public List<Recording> getRecordings(){
        return recordingService.readAllRecording();
    }

    @PostMapping("/post")
    public ResponseEntity<?> postRecording(@Valid @RequestBody Recording recording){
        boolean added = recordingService.createRecording(recording);
        if(added){
            return ResponseEntity.ok("Recording Added");
        }else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/put/{uuid}")
    public ResponseEntity<?> putRecording(@PathVariable UUID uuid, @Valid @RequestBody Recording recording){
        try{
            return ResponseEntity.ok(recordingService.updateRecording(uuid,recording));
        }catch(RecordingNotFoundException exception){
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }catch(Exception exception){
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<?> deleteRecording(@PathVariable UUID uuid){
        try{
            recordingService.deleteRecording(uuid);
            return ResponseEntity.ok().build();
        }catch(RecordingNotFoundException exception){
            throw new ResponseStatusException(NOT_FOUND, exception.getMessage());
        }catch(Exception exception){
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }
}
