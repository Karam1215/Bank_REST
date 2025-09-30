package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO для возврата баланса карты")
public record CardBalanceDTO(
        @Schema(description = "Баланс карты", example = "15000.50")
        BigDecimal balance
) {}
