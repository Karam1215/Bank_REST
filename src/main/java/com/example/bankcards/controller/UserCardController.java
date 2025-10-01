package com.example.bankcards.controller;

import com.example.bankcards.dto.CardResponseDTO;
import com.example.bankcards.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/cards")
@Tag(name = "API для пользователя", description = "Позволяет пользователю просматривать свои карты")
public class UserCardController {

    private final CardService cardService;

    @Operation(
            summary = "Получить свои карты",
            description = "Возвращает список карт текущего авторизованного пользователя с поддержкой пагинации"
    )
    @GetMapping("")
    public ResponseEntity<Page<CardResponseDTO>> getUserCards(
            @Parameter(description = "Параметры пагинации (page, size, sort)", example = "page=0&size=10&sort=expirationDate,desc")
            Pageable pageable
    ) {
        log.info("Запрос на получение карт текущего пользователя с пагинацией: {}", pageable);
        return cardService.getUserCards(pageable);
    }
    @GetMapping("/search")
    public ResponseEntity<Page<CardResponseDTO>> searchCards(
            @RequestParam String cardNumber,
            @PageableDefault(size = 5) Pageable pageable) {

        return cardService.searchCardsByNumber(cardNumber, pageable);
    }
}
