package com.example.bankcards.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UserShortDTOForCard(
        UUID userId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDate birthDate
) {}