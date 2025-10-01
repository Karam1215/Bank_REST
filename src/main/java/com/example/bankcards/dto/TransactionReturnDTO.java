package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO транзакции для администратора")
public record TransactionReturnDTO(
        UUID transactionId,
        BigDecimal amount,
        LocalDateTime createdAt,
        String originCardNumber,
        String destinationCardNumber,
        UserShortDTOForCard user
) {}
