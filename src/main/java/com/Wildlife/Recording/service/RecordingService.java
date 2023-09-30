package com.Wildlife.Recording.service;

import com.Wildlife.Recording.repository.RecordingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordingService {

    @Autowired
    private RecordingRepository recordingRepository;

    public RecordingService(RecordingRepository recordingRepository)
    {
        this.recordingRepository = recordingRepository;
    }
}
