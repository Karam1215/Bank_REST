package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "DTO для обновления карты администратором")
public record CardUpdateDTO(

        @Schema(description = "Новый статус карты", example = "BLOCKED")
        String status,

        @Schema(description = "Новый срок действия карты", example = "2028-12-31")
        LocalDate expirationDate
) {}
