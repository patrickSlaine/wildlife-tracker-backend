package com.Wildlife.Recording.controller;

import com.Wildlife.Recording.service.RecordingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recording")
public class RecordingController {

    @Autowired
    private RecordingService recordingService;

}
