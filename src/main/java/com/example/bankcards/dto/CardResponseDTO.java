package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO для ответа при работе с картой")
public record CardResponseDTO(
        @Schema(description = "Идентификатор карты") UUID cardId,
        @Schema(description = "Идентификатор владельца карты") UUID userId,
        @Schema(description = "Номер карты (замаскированный)") String maskedCardNumber,
        @Schema(description = "Срок действия карты") LocalDate expirationDate,
        @Schema(description = "Статус карты") String status,
        @Schema(description = "Баланс карты") BigDecimal balance,
        @Schema(description = "Дата создания карты") LocalDateTime createdAt,
        @Schema(description = "Дата последнего обновления карты") LocalDateTime updatedAt,
        @Schema(description = "Кто создал карту") UUID createdBy
) {}
