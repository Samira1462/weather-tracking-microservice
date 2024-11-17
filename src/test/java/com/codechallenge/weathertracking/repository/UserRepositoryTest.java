package com.codechallenge.weathertracking.repository;

import com.codechallenge.weathertracking.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository systemUnderTest;

    @BeforeEach
    void setUp() {
        systemUnderTest.save(new UserEntity(null, "john_doe", "12345", LocalDateTime.now(), List.of()));
        systemUnderTest.save(new UserEntity(null, "john_rew", "67890", LocalDateTime.now(), List.of()));
    }

    @Test
    void findByUsername_whenUsernameExists_thenReturnUser() {
        Optional<UserEntity> result = systemUnderTest.findByUsername("john_doe");

        assertTrue(result.isPresent());
        assertEquals("john_doe", result.get().getUsername());
        assertEquals("12345", result.get().getPostalCode());
    }

    @Test
    void findByUsername_whenUsernameDoesNotExist_thenReturnEmptyOptional() {
        Optional<UserEntity> result = systemUnderTest.findByUsername("nonexistentUser");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByPostalCode_whenPostalCodeExists_thenReturnUser() {

        Optional<UserEntity> result = systemUnderTest.findByPostalCode("67890");

        assertTrue(result.isPresent());
        assertEquals("john_rew", result.get().getUsername());
        assertEquals("67890", result.get().getPostalCode());
    }

    @Test
    void findByPostalCode_whenPostalCodeDoesNotExist_thenReturnEmptyOptional() {
        Optional<UserEntity> result = systemUnderTest.findByPostalCode("99999");

        assertTrue(result.isEmpty());
    }
}
