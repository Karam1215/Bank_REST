package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "DTO для создания банковской карты администратором")
public record CreateCardDTO(

    @NotNull(message = "Пользователь обязателен")
    @Schema(name = "userId", example = "d290f1ee-6c54-4b01-90e6-d701748f0851", description = "Идентификатор пользователя, которому принадлежит карта")
    UUID userId,

    @NotBlank(message = "Номер карты обязателен")
    @Pattern(regexp = "\\d{4}-\\d{4}-\\d{4}-\\d{4}", message = "Номер карты должен быть в формате XXXX-XXXX-XXXX-XXXX")
    @Schema(name = "cardNumber", example = "1234-5678-9012-3456", description = "Номер карты")
    String cardNumber,

    @NotNull(message = "Срок действия карты обязателен")
    @Schema(name = "expirationDate", example = "2026-12-31", description = "Срок действия карты")
    LocalDate expirationDate,

    @NotBlank(message = "Статус карты обязателен")
    @Schema(name = "status", example = "ACTIVE", description = "Статус карты")
    String status,

    @NotNull(message = "Баланс карты обязателен")
    @DecimalMin(value = "0.0", inclusive = true, message = "Баланс карты не может быть отрицательным")
    @Schema(name = "balance", example = "15000.50", description = "Баланс карты")
    BigDecimal balance

) {}
