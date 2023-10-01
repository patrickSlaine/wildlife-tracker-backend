package com.Wildlife.Recording.service;


import com.Wildlife.Recording.model.entities.Recording;
import com.Wildlife.Recording.model.exceptions.RecordingNotFoundException;
import com.Wildlife.Recording.repository.RecordingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecordingManagementServiceTests {

    @Mock
    private RecordingRepository recordingRepository;

    @InjectMocks
    private RecordingService recordingService;

    private Recording recording;

    @BeforeEach
    public void setup(){
        recording = new Recording(null,"Kangaroo",1.0000,2.0000,"/folderOne/kangaroo.png", UUID.randomUUID());
    }

    @Test
    public void readRecordingValid(){
        //Prepare
        given(recordingRepository.findById(recording.getId()))
                .willReturn(Optional.of(recording));

        //Act
        Recording result = recordingService.readRecording(recording.getId());

        //Assert
        assertEquals(result,recording);
    }

    @Test
    public void readRecordingInvalid(){
        //Prepare
        UUID uuid = UUID.randomUUID();
        given(recordingRepository.findById(uuid)).willThrow(RecordingNotFoundException.class);

        // Act
        try{
            recordingService.readRecording(uuid);
        }catch(RecordingNotFoundException exception){
            return;
        }
    }

    @Test
    public void createRecordingValid(){
        //Prepare
        /*given(recordingRepository.save(recording));*/
        given(recordingRepository.findById(recording.getId())).willReturn(Optional.of(recording));

        //Act
        boolean added = recordingService.createRecording(recording);

        //Assert
        assertTrue(added);
    }

    @Test
    public void updateRecordingValid(){
        //Prepare
        given(recordingRepository.existsById(recording.getId())).willReturn(true);
        given(recordingRepository.findById(recording.getId()))
                .willReturn(Optional.of(new Recording()));
        given(recordingRepository.save(recording)).willReturn(recording);

        //Act
        Recording results = recordingService.updateRecording(recording.getId(),recording);

        //Assert
        assertEquals(results, recording);
    }

    @Test
    public void updateRecordingInvalid(){
        //Prepare
        given(recordingRepository.existsById(recording.getId())).willReturn(false);

        //Act
        try{
            recordingService.updateRecording(recording.getId(),recording);
        }catch(RecordingNotFoundException exception){
            return;
        }
        fail();
    }

    @Test
    public void deleteUserValid(){
        //Prepare
        given(recordingRepository.findById(recording.getId())).willReturn(Optional.of(recording));

        //Act & Assert
        try{
            recordingService.deleteRecording(recording.getId());
        }catch(RecordingNotFoundException exception) {
            fail();
        }
    }

    @Test
    public void deleteUserInvalid(){
        //Prepare
        given(recordingRepository.findById(recording.getId())).willReturn(Optional.empty());

        //Act & Assert
        try{
            recordingService.deleteRecording(recording.getId());
        }catch(RecordingNotFoundException exception){
            return;
        }
        fail();

    }

    @Test
    public void readUsersValid(){
        //Prepare
        List<Recording> recordings = List.of(
                new Recording(
                        null,
                        "Kangaroo",
                        1.0000,
                        2.0000,
                        "/folderOne/kangaroo.png",
                        UUID.randomUUID()),
                new Recording(
                        null,
                        "Tiger",
                        1.5000,
                        2.0000,
                        "/folderOne/tiger.png",
                        UUID.randomUUID()));

        given(recordingRepository.findAll()).willReturn(recordings);

        //Act
        List<Recording> testData = recordingService.readAllRecording();

        //Assert
        assertEquals(recordings.size(),testData.size());
        assertEquals(recordings,testData);
    }


}
