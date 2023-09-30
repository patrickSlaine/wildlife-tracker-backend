package com.Wildlife.Recording.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity(name="User")
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id",nullable = false,unique = true)
    private UUID id;
    @Column(name="userName", nullable = false, unique = true)
    private String userName;
    @Column(name="firstName",nullable = false)
    private String firstName;
    @Column(name="lastName",nullable = false)
    private String lastName;
    @Column(name="dateOfBirth",nullable = false)
    private Date dateOfBirth;
    @Column(name="joiningDate",nullable = false)
    private Date joiningDate;
    @OneToOne(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name="passwordHash_id", referencedColumnName = "id")
    @JsonIgnore
    private PasswordHash passwordHash;

    public User(UUID id, String userName, String firstName, String lastName, Date dateOfBirth, Date joiningDate, PasswordHash passwordHash) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.joiningDate = joiningDate;
        this.passwordHash = passwordHash;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    public PasswordHash getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(PasswordHash passwordHash) {
        this.passwordHash = passwordHash;
    }
}
