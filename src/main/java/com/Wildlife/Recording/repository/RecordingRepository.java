package com.Wildlife.Recording.repository;

import com.Wildlife.Recording.model.entities.Recording;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecordingRepository extends JpaRepository<Recording, UUID> {
}
