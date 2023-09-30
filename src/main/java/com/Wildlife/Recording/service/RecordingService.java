package com.Wildlife.Recording.service;

import com.Wildlife.Recording.model.entities.Recording;
import com.Wildlife.Recording.model.exceptions.RecordingNotFoundException;
import com.Wildlife.Recording.repository.RecordingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecordingService {

    @Autowired
    private RecordingRepository recordingRepository;

    public RecordingService(RecordingRepository recordingRepository)
    {
        this.recordingRepository = recordingRepository;
    }

    public boolean createRecording(Recording recording){
        recordingRepository.save(recording);
        return recordingRepository.findById(recording.getId()).isPresent();
    }

    public Recording updateRecording(UUID uuid, Recording recording){
        if(!recordingRepository.existsById(uuid)){
            throw new RecordingNotFoundException("Recording with UUID " + uuid + " is not found.");
        }
        else{
            Recording r = recordingRepository.findById(uuid).orElseThrow();
            r.setSpeciesName(recording.getSpeciesName());
            r.setSubmittedBy(recording.getSubmittedBy());
            r.setImagePath(recording.getImagePath());
            r.setLatitude(recording.getLatitude());
            r.setLongitude(recording.getLongitude());

            return recordingRepository.save(r);
        }
    }

    public Recording readRecording(UUID uuid){
        Optional<Recording> recording = recordingRepository.findById(uuid);
        if (recording.isPresent()){
            return recording.get();
        }
        else{
            throw new RecordingNotFoundException("Recording with UUID " + uuid + " is not found.");
        }
    }

    public void deleteRecording(UUID uuid){
        Optional<Recording> recording = recordingRepository.findById(uuid);

        if (recording.isPresent()){
            recordingRepository.delete(recording.get());
        }
        else{
            throw new RecordingNotFoundException("Recording with UUID " + uuid + " is not found.");
        }
    }

    public List<Recording> readAllRecording(){
        return recordingRepository.findAll();
    }


}
