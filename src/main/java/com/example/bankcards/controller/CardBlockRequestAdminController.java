
package com.example.bankcards.controller;

import com.example.bankcards.dto.CardBlockRequestDTO;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.service.CardBlockRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/block-requests")
@Tag(name = "API для управления запросами на блокировку карт", description = "Админ может просматривать и обрабатывать запросы на блокировку карт")
@RequiredArgsConstructor
@Slf4j
public class CardBlockRequestAdminController {

    private final CardBlockRequestService cardBlockRequestService;

    @Operation(summary = "Получить все запросы на блокировку карт", description = "Администратор может просмотреть все запросы, фильтровать по статусу")
    @GetMapping
    public ResponseEntity<List<CardBlockRequestDTO>> getAllBlockRequests(
            @RequestParam(required = false) String status
    ) {
        return cardBlockRequestService.getAllRequests();
    }

    @Operation(summary = "Обработать запрос на блокировку карты", description = "Администратор может одобрить или отклонить запрос")
    @PutMapping("/{requestId}")
    public ResponseEntity<String> processBlockRequest(
            @PathVariable Long requestId,
            @RequestParam String action
    ) {
        return cardBlockRequestService.processBlockRequest(requestId, action);
    }
}
