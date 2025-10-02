package com.example.bankcards.entity;

import com.example.bankcards.converters.CardNumberEncryptor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cards")
@Schema(description = "Сущность банковской карты")
public class Card {

    @Transient
    private String maskedCardNumber;

    @Id
    @Column(name = "card_id", nullable = false, updatable = false)
    @GeneratedValue
    @Schema(name = "cardId", example = "d290f1ee-6c54-4b01-90e6-d701748f0851", description = "Уникальный идентификатор карты.")
    private UUID cardId;

    @NotNull(message = "Пользователь обязателен")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_cards_users"))
    @Schema(name = "userId", description = "Пользователь, которому принадлежит карта")
    private User user;

    @Convert(converter = CardNumberEncryptor.class)
    @Column(name = "card_number", unique = true, nullable = false, length = 50)
    @Schema(name = "cardNumber", example = "1234-5678-9012-3456", description = "Номер карты. Должен быть уникальным.")
    private String cardNumber;

    @Column(name = "expiration_date")
    @Schema(name = "expirationDate", example = "2026-12-31", description = "Срок действия карты")
    private LocalDate expirationDate;

    @Column(name = "status", length = 50)
    @Schema(name = "status", example = "ACTIVE", description = "Статус карты")
    private String status;

    @Column(name = "balance", precision = 15, scale = 2)
    @Schema(name = "balance", example = "15000.50", description = "Баланс карты")
    private BigDecimal balance;

    @Column(name = "created_at", updatable = false)
    @Schema(name = "createdAt", example = "2025-02-07T14:30:00", description = "Дата создания карты")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_cards_created_by"))
    @Schema(name = "createdBy", description = "Пользователь, создавший карту")
    private User createdBy;

    @Column(name = "updated_at")
    @Schema(name = "updatedAt", example = "2025-02-07T14:30:00", description = "Дата последнего обновления карты")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "Error";
        }
        String last4 = cardNumber.substring(cardNumber.length() - 4);
        return "****-****-****-" + last4;
    }
}