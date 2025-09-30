package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO для возврата информации о карте с ограниченной информацией о пользователе
 */
@Schema(description = "DTO для возврата информации о карте с ограниченной информацией о пользователе")
public record CardResponseDTO(
        UUID cardId,
        String cardNumber,
        LocalDate expirationDate,
        BigDecimal balance,
        String status,
        UserShortDTOForCard user
) {}
