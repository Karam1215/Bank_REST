package com.example.bankcards.service;

import com.example.bankcards.dto.CreateCardDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.Role;
import com.example.bankcards.exception.CardAlreadyExistException;
import com.example.bankcards.exception.InvalidCredentialsException;
import com.example.bankcards.mappers.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
}
