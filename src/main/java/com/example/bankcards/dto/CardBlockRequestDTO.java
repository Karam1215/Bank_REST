package com.example.bankcards.dto;

import java.time.LocalDateTime;

/**
 * DTO для возврата информации о запросе на блокировку карты
 */
public record CardBlockRequestDTO(
        Long requestId,
        CardShortDTOForRequest card,
        UserShortDTOForRequest user,
        String status,
        LocalDateTime createdAt,
        LocalDateTime processedAt,
        UserShortDTOForRequest processedBy
) {}
