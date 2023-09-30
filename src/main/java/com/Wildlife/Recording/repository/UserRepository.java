package com.Wildlife.Recording.repository;

import com.Wildlife.Recording.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
