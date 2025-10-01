package com.example.bankcards.service;

import com.example.bankcards.JWT.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class JWTServiceTest {

    @Autowired
    private JWTService jwtService;

    @Test
    void testGenerateAndExtractToken() {
        UUID userId = UUID.randomUUID();
        String role = "USER";

        String token = jwtService.generateAccessToken(userId, role);
        String extractedUserId = jwtService.extractUserName(token);
        String extractedRole = jwtService.extractRole(token);

        assertEquals(userId.toString(), extractedUserId);
        assertEquals("ROLE_USER", extractedRole);
    }
}
