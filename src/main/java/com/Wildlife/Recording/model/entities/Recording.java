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
    @Column(name="submittedBY",nullable = false)
    private UUID submittedBy;

    public Recording(UUID id, String speciesName, Double latitude, Double longitude, String imagePath, UUID submittedBy) {
        this.id = id;
        this.speciesName = speciesName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imagePath = imagePath;
        this.submittedBy = submittedBy;
    }
    public Recording(){

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public UUID getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(UUID submittedBy) {
        this.submittedBy = submittedBy;
    }
}
