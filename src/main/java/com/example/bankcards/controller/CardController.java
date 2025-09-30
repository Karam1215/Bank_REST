package com.example.bankcards.controller;

import com.example.bankcards.dto.CreateCardDTO;
import com.example.bankcards.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/cards")
@Tag(name = "API для управления банковскими картами", description = "Позволяет создавать, получать, обновлять и удалять карты")
public class CardController {

    private final CardService cardService;

    @Operation(summary = "Создать новую карту", description = "Позволяет создать новую банковскую карту для пользователя")
    @PostMapping("/create")
    public ResponseEntity<String> createCard(@RequestBody CreateCardDTO cardDTO) {
        log.info("Запрос на создание карты: {}", cardDTO);
        return cardService.createCard(cardDTO);
    }

}
