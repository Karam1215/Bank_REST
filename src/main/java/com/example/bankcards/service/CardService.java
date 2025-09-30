package com.example.bankcards.service;

import com.example.bankcards.dto.CardBalanceDTO;
import com.example.bankcards.dto.CardResponseDTO;
import com.example.bankcards.dto.CardUpdateDTO;
import com.example.bankcards.dto.CreateCardDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.Role;
import com.example.bankcards.exception.CardAlreadyExistException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InvalidCredentialsException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.mappers.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

    private final CardMapper cardMapper;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public ResponseEntity<String> createCard(CreateCardDTO createCardDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        log.info("ID аутентифицированного пользователя (админа): {}", userId);
        UUID uuid = UUID.fromString(userId);

        User admin = userRepository.findUserByUserId(uuid)
                .orElseThrow(() -> new InvalidCredentialsException("Админ не найден"));

        log.info("Админ: {}", admin);

        if(!admin.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("У пользователя нет прав администратора");
        }

        if(cardRepository.findByCardNumber(createCardDTO.cardNumber()).isPresent()) {
            throw new CardAlreadyExistException("Карта с таким номером уже существует");
        }

        User user = userRepository.findById(createCardDTO.userId())
                .orElseThrow(() -> new InvalidCredentialsException("Пользователь не найден"));

        if (createCardDTO.balance().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("Попытка создать карту с отрицательным балансом: {}", createCardDTO.balance());
            throw new IllegalArgumentException("Баланс карты не может быть отрицательным");
        }

        Card card = Card.builder()
                .user(user)
                .cardNumber(createCardDTO.cardNumber())
                .expirationDate(createCardDTO.expirationDate())
                .balance(createCardDTO.balance())
                .status("ACTIVE")
                .createdBy(admin)
                .build();

        cardRepository.save(card);
        log.info("Карта успешно создана для пользователя {} с номером {}", user.getUserId(), createCardDTO.cardNumber());

        return new ResponseEntity<>("🎊 Карта успешно создана 🚀", HttpStatus.CREATED);
    }

    public ResponseEntity<List<CardResponseDTO>> getAllCards() {
        List<Card> cards = cardRepository.findAll();

        List<CardResponseDTO> cardResponseDTOS = cardMapper.toResponseDtoList(cards);

        log.info("Запрошен список всех карт, найдено: {}", cardResponseDTOS.size());

        return ResponseEntity.ok(cardResponseDTOS);
    }

    public ResponseEntity<CardResponseDTO> getCardByCardId(String cardId) {
        UUID uuid = UUID.fromString(cardId);
        Card card = cardRepository.findCardsByCardId(uuid)
                .orElseThrow(() -> new CardNotFoundException("card not found"));

        CardResponseDTO cardResponseDTO = cardMapper.toResponseDto(card);
        return ResponseEntity.ok(cardResponseDTO);
    }

    public ResponseEntity<String> deleteCardByCardId(String cardId) {
        UUID uuid = UUID.fromString(cardId);
        Card card = cardRepository.findCardsByCardId(uuid).orElseThrow(() -> {
            log.warn("Card not found for deletion: {}", cardId);
            throw new CardNotFoundException("Card not found for deletion");
        });

        cardRepository.delete(card);
        log.info("card deleted: {} 🎉🎉🎉🎉🎉🎉", card);

        return new ResponseEntity<>("card deleted successfully", HttpStatus.OK);
    }

    public ResponseEntity<CardResponseDTO> updateCard(String cardId, CardUpdateDTO dto) {
        Card card = cardRepository.findById(UUID.fromString(cardId))
                .orElseThrow(() -> new CardNotFoundException("Карта с id " + cardId + " не найдена"));

        if (dto.status() != null) {
            card.setStatus(dto.status());
        }

        if (dto.expirationDate() != null) {
            card.setExpirationDate(dto.expirationDate());
        }

        cardRepository.save(card);

        log.info("Карта {} обновлена администратором", cardId);

        return ResponseEntity.ok(cardMapper.toResponseDto(card));
    }

    public ResponseEntity<Page<CardResponseDTO>> getUserCards(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        UUID uuid = UUID.fromString(userId);

        User user = userRepository.findUserByUserId(uuid)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Page<Card> cards = cardRepository.findByUser(user, pageable);

        Page<CardResponseDTO> cardResponseDTOs = cards.map(cardMapper::toResponseDto);

        log.info("Пользователь {} запросил свои карты, найдено {}", user.getEmail(), cardResponseDTOs.getTotalElements());

        return ResponseEntity.ok(cardResponseDTOs);
    }
    public ResponseEntity<CardBalanceDTO> getCardBalance(UUID cardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        UUID userIdFromToken = UUID.fromString(userId);

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Карта не найдена"));

        if (!card.getUser().getUserId().equals(userIdFromToken)) {
            throw new AccessDeniedException("У вас нет доступа к этой карте");
        }

        log.info("Пользователь {} запросил баланс карты {}", userIdFromToken, cardId);

        return ResponseEntity.ok(new CardBalanceDTO(card.getBalance()));
    }
}
