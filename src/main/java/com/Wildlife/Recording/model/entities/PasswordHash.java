package com.Wildlife.Recording.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.UUID;

@Entity(name="passwordHash")
public class PasswordHash {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id",nullable = false,unique = true)
    private UUID id;
    @Column(name="hashValue",nullable = false)
    private String hashValue;

    @Column(name="salt", nullable = true)
    private String salt;

    @OneToOne
    @JsonIgnore
    private User user;

    public PasswordHash(UUID id, String hashValue, String salt, User user){
        this.id = id;
        this.hashValue = hashValue;
        this.salt = salt;
        this.user = user;
    }

    public PasswordHash(UUID id, String hashValue, String salt){
        this.id = id;
        this.hashValue = hashValue;
        this.salt = salt;
    }

    public PasswordHash(String hashValue, String salt){
        this.hashValue = hashValue;
        this.salt = salt;
    }

    public PasswordHash(){
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public User getUser() {
        return user;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
