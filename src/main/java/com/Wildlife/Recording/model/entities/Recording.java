package com.Wildlife.Recording.model.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity(name="Recording")
@Table(name="Recording")
public class Recording {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id", nullable=false, unique=true)
    private UUID id;
    @Column(name="name",nullable = false,length=100)
    private String speciesName;
    @Column(name="latitude", nullable = false)
    private Double latitude;
    @Column(name="longitude",nullable = false)
    private Double longitude;
    @Column(name="imagePath", nullable = false)
    private String imagePath;
    @ManyToOne
    @JoinColumn
    private User submittedBy;


}
