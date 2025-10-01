package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Запрос на перевод средств между своими картами")
public record TransferRequestDTO(
        @Schema(description = "ID исходной карты", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
        UUID originCardId,

        @Schema(description = "ID карты получателя", example = "72bde859-d59e-4a61-b060-d5fa60426203")
        UUID destinationCardId,

        @Schema(description = "Сумма перевода", example = "500.00")
        BigDecimal amount
) {}
