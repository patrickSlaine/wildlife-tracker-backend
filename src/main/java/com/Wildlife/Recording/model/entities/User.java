package com.Wildlife.Recording.model.entities;

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
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="passwordHash_id", referencedColumnName = "id")
    private PasswordHash passwordHash;

}
