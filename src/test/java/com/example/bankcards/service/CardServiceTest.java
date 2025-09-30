package com.example.bankcards.service;

import com.example.bankcards.dto.CreateCardDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.Role;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTest {

    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void testCreateCard_AdminSuccess() {
        // Mock admin user in token
        UUID adminId = UUID.randomUUID();
        when(authentication.getName()).thenReturn(adminId.toString());

        User admin = new User();
        admin.setUserId(adminId);
        admin.setRole(Role.valueOf("ADMIN"));

        when(userRepository.findUserByUserId(adminId)).thenReturn(Optional.of(admin));

        // Mock target user
        UUID userId = UUID.randomUUID();
        User targetUser = new User();
        targetUser.setUserId(userId);
        targetUser.setRole(Role.valueOf("USER"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(targetUser));

        // Create DTO
        CreateCardDTO dto = new CreateCardDTO(userId, "1234-5678-9012-3456",
                LocalDate.now().plusYears(3),"ACTIVE" ,BigDecimal.valueOf(10000));

        // Call service
        ResponseEntity<String> response = cardService.createCard(dto);

        // Verify
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("🎊 Card created 🚀", response.getBody());
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void testCreateCard_NotAdmin_ThrowsException() {
        UUID userId = UUID.randomUUID();
        when(authentication.getName()).thenReturn(userId.toString());

        User notAdmin = new User();
        notAdmin.setUserId(userId);
        notAdmin.setRole(Role.valueOf("USER"));

        when(userRepository.findUserByUserId(userId)).thenReturn(Optional.of(notAdmin));

        CreateCardDTO dto = new CreateCardDTO(UUID.randomUUID(), "1234-5678-9012-3456",
                LocalDate.now().plusYears(3),"ACTIVE" ,BigDecimal.valueOf(10000));

        assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            cardService.createCard(dto);
        });

        verify(cardRepository, never()).save(any());
    }
}
