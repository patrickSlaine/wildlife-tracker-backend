package com.Wildlife.Recording.controller;

import com.Wildlife.Recording.model.entities.PasswordHash;
import com.Wildlife.Recording.model.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.sql.Array;
import java.util.*;

import static com.Wildlife.Recording.configuration.DBSeeder.DB_SEEDER_USERS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"h2","dbseeder"})
public class UserControllerTests {

    private static final String USER_ENDPOINT_URL = "/api/user";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void getUserByIdValid() throws Exception {
        User user = DB_SEEDER_USERS.get(0);

        String JSON = this.mockMvc
                .perform(get(USER_ENDPOINT_URL+"/get/" + user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User testData = objectMapper.readValue(JSON, new TypeReference<User>(){});

        assertNotNull(testData);
        assertEquals(user.getId(),testData.getId());
    }

    @Test
    public void getUserByIdInvalid() throws Exception{
        UUID uuid = UUID.randomUUID();

        this.mockMvc
            .perform(get(USER_ENDPOINT_URL+"/get/" + uuid))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    public void getUsersValid() throws Exception{

        String JSON = this.mockMvc
                    .perform(get(USER_ENDPOINT_URL+"/get"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        List<User> testData = objectMapper.readValue(JSON,new TypeReference<List<User>>(){});

        assertNotNull(testData);
        assertEquals(testData.size(),DB_SEEDER_USERS.size());
    }

    @Test
    @DirtiesContext
    @Rollback
    public void postUserValid() throws Exception{
        //Prepare
        User user = new User(
                null,
                "NewTestUser12",
                "Joe",
                "Bloggs",
                new Date("10/12/2000"),
                new Date("01/10/2023"),
                new PasswordHash(
                        null,
                        "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8")
        );
        String JSON = objectMapper.writeValueAsString(user);

        List<User> before = getAllUsers();
        //Act
        this.mockMvc
            .perform(
                    post(USER_ENDPOINT_URL+"/post")
                    .header("Content-Type","application/json")
                    .content(JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        //Assert
        List<User> after = getAllUsers();
        assertEquals(before.size(), after.size()-1);
    }

    @Test
    @DirtiesContext
    @Rollback
    public void postUserInvalidNoJson() throws Exception {

        List<User> before = getAllUsers();
        //Act
        this.mockMvc
                .perform(
                        post(USER_ENDPOINT_URL+"/post")
                                .header("Content-Type","application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //Assert
        List<User> after = getAllUsers();
        assertEquals(before.size(), after.size());
    }

    @Test
    @DirtiesContext
    @Rollback
    public void postUserInvalidIncorrectJson() throws Exception {
        Dictionary<String,String> testData = new Hashtable<>();
        testData.put("Test","Test");

        String JSON = objectMapper.writeValueAsString(testData);

        List<User> before = getAllUsers();
        //Act
        this.mockMvc
                .perform(
                        post(USER_ENDPOINT_URL+"/post")
                                .header("Content-Type","application/json")
                                .content(JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //Assert
        List<User> after = getAllUsers();
        assertEquals(before.size(), after.size());
    }

    @Test
    @DirtiesContext
    @Rollback
    public void putUserValid() throws Exception {
        //Prepare
        User user = DB_SEEDER_USERS.get(0);
        user.setUserName("Bruce Lee");
        String JSON = objectMapper.writeValueAsString(user);

        //Act
        JSON = mockMvc.perform(
                    put(USER_ENDPOINT_URL+"/put/"+user.getId())
                        .header("Content-Type","application/json")
                        .content(JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //Assert
        User resultsUser = objectMapper.readValue(JSON, new TypeReference<User>() {});

        assertNotNull(resultsUser);
        assertEquals(user.hashCode(),resultsUser.hashCode());
    }

    @Test
    @DirtiesContext
    @Rollback
    public void putUserPasswordValid() throws Exception {
        //Prepare
        User user = DB_SEEDER_USERS.get(0);
        user.setUserName("Bruce Lee");
        user.setPasswordHash(new PasswordHash(
                            null,
                            "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"));
        String JSON = objectMapper.writeValueAsString(user);

        //Act
        JSON = mockMvc.perform(
                        put(USER_ENDPOINT_URL+"/put/"+user.getId())
                                .header("Content-Type","application/json")
                                .content(JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //Assert
        User resultsUser = objectMapper.readValue(JSON, new TypeReference<User>() {});

        assertNotNull(resultsUser);
        assertEquals(user.hashCode(),resultsUser.hashCode());
    }

    @Test
    @DirtiesContext
    @Rollback
    public void putUserInvalidUserId() throws Exception {
        //Prepare
        User user = DB_SEEDER_USERS.get(0);
        String JSON = objectMapper.writeValueAsString(user);

        List<User> before = getAllUsers();

        //Act
        mockMvc.perform(
                        put(USER_ENDPOINT_URL+"/put/"+UUID.randomUUID())
                        .header("Content-Type","application/json")
                        .content(JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //Assert
        List<User> after = getAllUsers();
        assertEquals(before.size(),after.size());
    }

    @Test
    public void putUserInvalidRequestBody() throws Exception{
        //Prepare
        User user = DB_SEEDER_USERS.get(0);
        Dictionary<String,String> testData = new Hashtable<>();
        testData.put("Test","Test");
        String JSON = objectMapper.writeValueAsString(testData);

        //Act
        mockMvc.perform(
                        put(USER_ENDPOINT_URL+"/put/"+user.getId())
                        .header("Content-Type","application/json")
                        .content(JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //Assert
        String jsonUserAfter = mockMvc.perform(
                get(USER_ENDPOINT_URL+"/get/"+user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        User userAfter = objectMapper.readValue(jsonUserAfter, new TypeReference<>(){});
        assertEquals(user.hashCode(), userAfter.hashCode());
    }

    @Test
    @DirtiesContext
    @Rollback
    public void deleteUserValid() throws Exception{
        //Prepare
        User user = DB_SEEDER_USERS.get(0);
        List<User> before = getAllUsers();

        //Act
        mockMvc.perform(delete(USER_ENDPOINT_URL+"/delete/"+user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //Assert
        List<User> after = getAllUsers();
        assertEquals(before.size(), after.size()+1);
    }

    @Test
    @DirtiesContext
    @Rollback
    public void deleteUserInvalidUserId() throws Exception{
        //Prepare
        List<User> before = getAllUsers();

        //Act
        mockMvc.perform(delete(USER_ENDPOINT_URL+"/delete/"+UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //Assert
        List<User> after = getAllUsers();
        assertEquals(before.size(),after.size());
    }

    private List<User> getAllUsers() throws Exception{
        String JSON = mockMvc.perform(get(USER_ENDPOINT_URL+"/get"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readValue(JSON, new TypeReference<List<User>>() {});
    }
}
