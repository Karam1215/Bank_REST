package com.example.bankcards.dto;

import com.example.bankcards.enums.Role;

import java.time.LocalDate;
import java.util.UUID;

public record UserProfileDTO(
        UUID userId,
        String firstName,
        String middleName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDate birthDate,
        String gender
) {}
