package com.example.bankcards.service;

import com.example.bankcards.dto.CardBlockRequestDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.mappers.CardBlockRequestMapper;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardBlockRequestService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardBlockRequestMapper mapper;
    private final CardBlockRequestRepository cardBlockRequestRepository;

    public ResponseEntity<String> requestCardBlock(UUID cardId) {
    Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new CardNotFoundException("Карта не найдена"));

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = UUID.fromString(authentication.getName());

        CardBlockRequest request = CardBlockRequest.builder()
                .card(card)
                .user(userRepository.findById(userId).orElseThrow())
                .status("PENDING")
                .build();
        cardBlockRequestRepository.save(request);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    public ResponseEntity<List<CardBlockRequestDTO>> getAllRequests() {
        List<CardBlockRequestDTO> dtoList = cardBlockRequestRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    public ResponseEntity<String> processBlockRequest(Long requestId, String action) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID adminId = UUID.fromString(authentication.getName());
        User admin = userRepository.findById(adminId).orElseThrow(
                    () -> new UserNotFoundException("user not found"));
        CardBlockRequest request = cardBlockRequestRepository.findByRequestId(requestId)
                .orElseThrow(() -> new RuntimeException("Запрос на блокировку не найден"));

        if (!request.getStatus().equals("PENDING")) {
            throw new RuntimeException("Запрос уже обработан");
        }

        if (action.equalsIgnoreCase("APPROVE")) {
            request.setStatus("APPROVED");
            Card card = request.getCard();
            card.setStatus("BLOCKED");
            cardRepository.save(card);
        } else if (action.equalsIgnoreCase("REJECT")) {
            request.setStatus("REJECTED");
        } else {
            throw new RuntimeException("Неверное действие. Должно быть APPROVE или REJECT");
        }

        request.setProcessedBy(admin);
        request.setProcessedAt(java.time.LocalDateTime.now());
        cardBlockRequestRepository.save(request);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
