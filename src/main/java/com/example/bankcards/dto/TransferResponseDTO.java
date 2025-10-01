package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Ответ после выполнения перевода")
public record TransferResponseDTO(
        UUID transactionId,
        UUID originCardId,
        UUID destinationCardId,
        BigDecimal amount,
        LocalDateTime createdAt
) {}
