package com.example.bankcards.dto;

import java.util.UUID;
import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * Минимальная информация о карте для отображения в запросах
 */
public record CardShortDTOForRequest(
        UUID cardId,
        String maskedCardNumber,
        LocalDate expirationDate,
        BigDecimal balance
) {}
