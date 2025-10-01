package com.example.bankcards.controller;

import com.example.bankcards.dto.CardResponseDTO;
import com.example.bankcards.dto.CardUpdateDTO;
import com.example.bankcards.dto.CreateCardDTO;
import com.example.bankcards.dto.TransactionReturnDTO;
import com.example.bankcards.service.CardBlockRequestService;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/cards")
@Tag(name = "API для управления банковскими картами",
     description = "Позволяет администратору создавать, получать, обновлять и удалять банковские карты")
public class AdminCardController {

    private final CardService cardService;
    private final CardBlockRequestService cardBlockRequestService;
    private final TransferService transferService;


    @Operation(
            summary = "Создать новую карту",
            description = "Администратор может создать новую банковскую карту для конкретного пользователя"
    )
    @PostMapping("/create")
    public ResponseEntity<String> createCard(
            @Valid @RequestBody CreateCardDTO cardDTO
    ) {
        log.info("Запрос на создание карты: {}", cardDTO);
        return cardService.createCard(cardDTO);
    }

    @Operation(
            summary = "Получить все карты",
            description = "Возвращает список всех карт вместе с основной информацией о владельцах"
    )
    @GetMapping("")
    public ResponseEntity<List<CardResponseDTO>> getAllCards() {
        log.info("Запрос на получение всех карт");
        return cardService.getAllCards();
    }

    @Operation(
            summary = "Получить карту по ID",
            description = "Возвращает информацию о карте по её уникальному идентификатору"
    )
    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponseDTO> getCardByCardId(
            @Parameter(description = "UUID карты", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable("cardId") String cardId
    ) {
        log.info("Запрос на получение карты с id {}", cardId);
        return cardService.getCardByCardId(cardId);
    }

    @Operation(
            summary = "Удалить карту",
            description = "Удаляет карту по её уникальному идентификатору"
    )
    @DeleteMapping("/{cardId}")
    public ResponseEntity<String> deleteCard(
            @Parameter(description = "UUID карты", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable("cardId") String cardId
    ) {
        log.info("Запрос на удаление карты {}", cardId);
        return cardService.deleteCardByCardId(cardId);
    }

    @Operation(
            summary = "Обновить карту",
            description = "Администратор может изменить только статус и срок действия карты"
    )
    @PutMapping("/{id}")
    public ResponseEntity<CardResponseDTO> updateCard(
            @Parameter(description = "UUID карты", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable String id,
            @Valid @RequestBody CardUpdateDTO dto
    ) {
        log.info("Запрос администратора на обновление карты {}", id);
        return cardService.updateCard(id, dto);
    }

    @PostMapping("/{cardId}/block-request")
    public ResponseEntity<String> requestCardBlock(
            @PathVariable UUID cardId) {
        return cardBlockRequestService.requestCardBlock(cardId);
    }

    @Operation(summary = "Получить все транзакции", description = "Администратор получает список транзакций с пагинацией")
    @GetMapping("/transactions")
    public ResponseEntity<Page<TransactionReturnDTO>> getAllTransactions(Pageable pageable) {
        log.info("Админ запросил список транзакций, page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return transferService.getAllTransactions(pageable);
    }
}
