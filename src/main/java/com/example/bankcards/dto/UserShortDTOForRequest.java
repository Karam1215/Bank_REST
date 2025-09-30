package com.example.bankcards.dto;

import java.util.UUID;

/**
 * Минимальная информация о пользователе для отображения в запросах
 */
public record UserShortDTOForRequest(
        UUID userId,
        String firstName,
        String lastName
) {}
