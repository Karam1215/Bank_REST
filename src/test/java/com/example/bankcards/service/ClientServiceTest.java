package com.example.bankcards.service;

import com.example.bankcards.JWT.JWTService;
import com.example.bankcards.cookies.CookieUtil;
import com.example.bankcards.dto.UserLoginDTO;
import com.example.bankcards.dto.UserProfileDTO;
import com.example.bankcards.dto.UserRegistrationDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.Role;
import com.example.bankcards.exception.EmailAlreadyExistException;
import com.example.bankcards.exception.InvalidCredentialsException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.mappers.UserMapper;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ClientServiceTest {

    @Mock
    private JWTService jwtService;

    @Mock
    private CookieUtil cookieUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private UserRegistrationDTO buildDTO() {
        return new UserRegistrationDTO(
                "karam",
                "kar",
                "karam@12155",
                "karam@gmail.com",
                "+718293189",
                LocalDate.of(1995, 5, 20),
                "MALE"
        );
    }

    @Test
    void createPlayer_ShouldSaveUser_WhenEmailNotExists() {
        UserRegistrationDTO dto = buildDTO();

        when(userRepository.findUserByEmail(dto.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPass");

        ResponseEntity<String> response = clientService.createClient(dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(dto.firstName(), savedUser.getFirstName());
        assertEquals(dto.lastName(), savedUser.getLastName());
        assertEquals(dto.email(), savedUser.getEmail());
        assertEquals("encodedPass", savedUser.getPassword());
        assertEquals(Role.USER, savedUser.getRole());

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Добро пожаловать"));
    }

    @Test
    void createPlayer_ShouldThrowException_WhenEmailAlreadyExists() {
        UserRegistrationDTO dto = buildDTO();

        when(userRepository.findUserByEmail(dto.email())).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExistException.class, () -> clientService.createClient(dto));

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        UserLoginDTO loginDTO = new UserLoginDTO("test@example.com", "password");

        User user = User.builder()
                .userId(UUID.randomUUID())
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .role(Role.USER)
                .build();

        when(userRepository.findUserByEmail(loginDTO.email())).thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(user.getUserId(), String.valueOf(Role.USER))).thenReturn("mockToken");

        ResponseEntity<String> response = clientService.login(loginDTO, null);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(cookieUtil).addAuthTokenCookie(any(), eq("mockToken"));

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Вход выполнен успешно"));
    }

    @Test
    void login_ShouldThrowException_WhenUserNotFound() {
        UserLoginDTO loginDTO = new UserLoginDTO("notfound@example.com", "password");

        when(userRepository.findUserByEmail(loginDTO.email())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> clientService.login(loginDTO, null));

        verify(authenticationManager, never()).authenticate(any());
        verify(cookieUtil, never()).addAuthTokenCookie(any(), anyString());
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findUserByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> clientService.getUserById(userId));

        verify(userRepository, times(1)).findUserByUserId(userId);
        verify(userMapper, never()).toDto(any());
    }
}
