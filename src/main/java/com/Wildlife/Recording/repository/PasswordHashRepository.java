package com.Wildlife.Recording.repository;

import com.Wildlife.Recording.model.entities.PasswordHash;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PasswordHashRepository extends JpaRepository<PasswordHash, UUID> {
}
