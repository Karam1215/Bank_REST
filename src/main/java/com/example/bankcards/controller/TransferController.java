package com.example.bankcards.controller;

import com.example.bankcards.dto.TransactionReturnDTO;
import com.example.bankcards.dto.TransferRequestDTO;
import com.example.bankcards.dto.TransferResponseDTO;
import com.example.bankcards.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/transfers")
@Tag(name = "API для переводов между своими картами", description = "Позволяет пользователю переводить средства между своими картами")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @Operation(summary = "Перевод между своими картами", description = "Позволяет перевести деньги между своими картами")
    public ResponseEntity<TransferResponseDTO> transfer(@Valid @RequestBody TransferRequestDTO dto) {
        log.info("Запрос перевода: {}", dto);
        return transferService.transfer(dto);
    }

    @GetMapping("/me")
    public ResponseEntity<Page<TransactionReturnDTO>> getMyTransactions(Pageable pageable) {
        return transferService.getTransactionsByUser(pageable);
    }
}
