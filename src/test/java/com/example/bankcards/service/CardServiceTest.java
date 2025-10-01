package com.example.bankcards.service;

import com.example.bankcards.dto.CardResponseDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.Role;
import com.example.bankcards.mappers.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private CardMapper cardMapper = CardMapper.INSTANCE;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User buildUser(UUID id) {
        return User.builder()
                .userId(id)
                .firstName("Иван")
                .lastName("Иванов")
                .email("ivan@example.com")
                .phoneNumber("+7(123)456-78-90")
                .birthDate(LocalDate.of(1990, 1, 1))
                .role(Role.USER)
                .build();
    }

    private Card buildCard(UUID cardId, User user) {
        return Card.builder()
                .cardId(cardId)
                .cardNumber("1234-5678-9012-3456")
                .expirationDate(LocalDate.of(2030, 12, 31))
                .balance(BigDecimal.valueOf(1000))
                .status("ACTIVE")
                .user(user)
                .build();
    }

    @Test
    void getAllCards_ShouldReturnListOfCardResponseDTO() {
        // given
        UUID userId = UUID.randomUUID();
        User user = buildUser(userId);
        Card card1 = buildCard(UUID.randomUUID(), user);
        Card card2 = buildCard(UUID.randomUUID(), user);

        when(cardRepository.findAll()).thenReturn(List.of(card1, card2));

        // when
        ResponseEntity<List<CardResponseDTO>> response = cardService.getAllCards();

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        CardResponseDTO dto = response.getBody().get(0);
        assertEquals(new String("****-****-****-" + card1.getCardNumber().substring(15)), dto.cardNumber());
        assertEquals(card1.getBalance(), dto.balance());
        assertEquals(card1.getExpirationDate(), dto.expirationDate());
        assertEquals("ACTIVE", dto.status());

        assertNotNull(dto.user());
        assertEquals(user.getUserId(), dto.user().userId());
        assertEquals(user.getFirstName(), dto.user().firstName());
        assertEquals(user.getEmail(), dto.user().email());

        verify(cardRepository, times(1)).findAll();
    }
}
