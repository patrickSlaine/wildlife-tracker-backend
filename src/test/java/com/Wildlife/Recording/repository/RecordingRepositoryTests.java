package com.Wildlife.Recording.repository;

import com.Wildlife.Recording.model.entities.Recording;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static com.Wildlife.Recording.configuration.DBSeeder.DB_SEEDER_RECORDINGS;

@DataJpaTest(properties = {"spring.jpa.hibernate.ddl-auto=update"})
@ActiveProfiles({"h2","dbseeder"})
public class RecordingRepositoryTests
{

    @Autowired
    private RecordingRepository recordingRepository;

    @Test
    public void findByIdValid(){
        Recording recording =new Recording(null,"Elephant",1.0000,2.0000,"/folderOne/elephant.png", UUID.randomUUID());
        recordingRepository.save(recording);
        Optional<Recording> response = recordingRepository.findById(recording.getId());
        Assertions.assertTrue(response.isPresent());
        Assertions.assertEquals(response.get().hashCode(), recording.hashCode());
    }

    @Test
    public void findByIdInvalid(){
        UUID uuid = UUID.randomUUID();
        Optional<Recording> response = recordingRepository.findById(uuid);
        Assertions.assertFalse(response.isPresent());
    }

}
