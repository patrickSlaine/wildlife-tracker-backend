package com.Wildlife.Recording.controller;

import com.Wildlife.Recording.model.entities.Recording;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import static com.Wildlife.Recording.configuration.DBSeeder.DB_SEEDER_RECORDINGS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"h2","dbseeder"})
public class RecordingControllerTests {

    private static final String RECORDING_ENDPOINT_URL = "/api/recording";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void getRecordingByIdValid() throws Exception{
        //Prepare
        Recording recording = DB_SEEDER_RECORDINGS.get(0);

        //Act
        String resultsJson = mockMvc.perform(get(RECORDING_ENDPOINT_URL+"/get/"+recording.getId()))
                                    .andDo(print())
                                    .andExpect(status().isOk())
                                    .andReturn()
                                    .getResponse()
                                    .getContentAsString();

        //Assert
        Recording results = objectMapper.readValue(resultsJson, new TypeReference<Recording>() {});
        assertNotNull(results);
        assertEquals(recording.hashCode(),results.hashCode());
    }

    @Test
    public void getRecordingByIdInvalidRecordingId() throws Exception{
        //Prepare
        UUID uuid = UUID.randomUUID();

        //Act & Assert
        mockMvc.perform(get(RECORDING_ENDPOINT_URL+"/get/"+uuid))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getRecordingsValid() throws Exception{
        //Prepare
        List<Recording> expected = DB_SEEDER_RECORDINGS;

        //Act
        String resultsJson = mockMvc.perform(get(RECORDING_ENDPOINT_URL+"/get"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<Recording> results = objectMapper.readValue(resultsJson, new TypeReference<List<Recording>>() {});

        //Assert
        assertNotNull(results);
        assertEquals(expected.size(), results.size());
    }

    @Test
    @DirtiesContext
    @Rollback
    public void postRecordingValid() throws Exception{
        //Prepare
        Recording testData = new Recording(
                null,
                "Squirrel",
                1.5400,
                2.3000,
                "/folderOne/squirrel.png",
                UUID.randomUUID());
        String testDataJson = objectMapper.writeValueAsString(testData);

        List<Recording> before = getAllRecordings();

        //Act
        mockMvc.perform(
                post(RECORDING_ENDPOINT_URL+"/post")
                .header("Content-Type","application/json")
                .content(testDataJson))
                .andDo(print())
                .andExpect(status().isOk());

        //Assert
        List<Recording> after = getAllRecordings();
        assertEquals(before.size(), after.size()-1);
    }

    @Test
    @DirtiesContext
    @Rollback
    public void putRecordingValid() throws Exception{
        //Prepare
        Recording recording = DB_SEEDER_RECORDINGS.get(0);
        recording.setSpeciesName("putRecordingValid");
        recording.setLongitude(0.0001);
        String JSON = objectMapper.writeValueAsString(recording);

        //Act
        String resultsJson = mockMvc.perform(
                    put(RECORDING_ENDPOINT_URL+"/put/"+recording.getId())
                    .header("Content-Type","application/json")
                    .content(JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //Assert
        Recording results = objectMapper.readValue(resultsJson, new TypeReference<Recording>() {});
        assertNotNull(results);
        assertEquals(recording.hashCode(), results.hashCode());
    }

    @Test
    @DirtiesContext
    @Rollback
    public void putRecordingInvalidRecordingId() throws Exception{
        //Prepare
        Recording recording = DB_SEEDER_RECORDINGS.get(0);
        String JSON = objectMapper.writeValueAsString(recording);
        List<Recording> before = getAllRecordings();
        //Act
        mockMvc.perform(
                        put(RECORDING_ENDPOINT_URL+"/put/"+UUID.randomUUID())
                                .header("Content-Type","application/json")
                                .content(JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        //Assert
        List<Recording> after = getAllRecordings();
        assertEquals(before.size(),after.size());

    }

    @Test
    @DirtiesContext
    @Rollback
    public void deleteRecordingValid() throws Exception{
        //Prepare
        Recording recording = DB_SEEDER_RECORDINGS.get(0);
        List<Recording> before = getAllRecordings();

        //Act
        mockMvc.perform(delete(RECORDING_ENDPOINT_URL+"/delete/"+recording.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        //Assert
        List<Recording> after = getAllRecordings();
        assertEquals(before.size(), after.size()+1);
    }

    @Test
    @DirtiesContext
    @Rollback
    public void deleteRecordingInvalidRecordingId() throws Exception{
        //Prepare
        List<Recording> before = getAllRecordings();

        //Act
        mockMvc.perform(delete(RECORDING_ENDPOINT_URL+"/delete/"+UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound());

        //Assert
        List<Recording> after = getAllRecordings();
        assertEquals(before.size(), after.size());
    }

    private List<Recording> getAllRecordings() throws Exception{
        String JSON = mockMvc.perform(get(RECORDING_ENDPOINT_URL+"/get"))
                                .andReturn()
                                .getResponse()
                                .getContentAsString();
        return objectMapper.readValue(JSON, new TypeReference<List<Recording>>() {});
    }
}
