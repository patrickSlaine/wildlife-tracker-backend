package com.Wildlife.Recording.service;

import com.Wildlife.Recording.repository.PasswordHashRepository;
import com.Wildlife.Recording.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordHashRepository passwordHashRepository;

    public UserManagementService(UserRepository userRepository,PasswordHashRepository passwordHashRepository)
    {
        this.userRepository = userRepository;
        this.passwordHashRepository = passwordHashRepository;
    }

}
