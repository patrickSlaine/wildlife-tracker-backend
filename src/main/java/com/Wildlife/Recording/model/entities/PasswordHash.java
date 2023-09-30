package com.Wildlife.Recording.model.entities;

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

    @OneToOne
    private User user;

    public PasswordHash(UUID id, String hashValue){
        this.id = id;
        this.hashValue = hashValue;
    }


}
