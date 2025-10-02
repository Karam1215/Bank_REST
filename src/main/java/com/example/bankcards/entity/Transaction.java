package com.example.bankcards.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
@Schema(description = "Сущность транзакции")
public class Transaction {

    @Id
    @Column(name = "transaction_id", nullable = false, updatable = false)
    @GeneratedValue
    @Schema(name = "transactionId", example = "e1c5b1d3-6c2f-4e6f-a3d8-123456789abc", description = "Уникальный идентификатор транзакции.")
    private UUID transactionId;

    @NotNull(message = "Исходная карта обязательна")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_card_id", nullable = false, foreignKey = @ForeignKey(name = "fk_tx_origin_card"))
    @Schema(name = "originCard", description = "Исходная карта транзакции")
    private Card originCard;

    @NotNull(message = "Карта получателя обязательна")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_card_id", nullable = false, foreignKey = @ForeignKey(name = "fk_tx_dest_card"))
    @Schema(name = "destinationCard", description = "Карта получателя транзакции")
    private Card destinationCard;

    @Column(name = "amount", precision = 15, scale = 2)
    @Schema(name = "amount", example = "1500.75", description = "Сумма транзакции")
    private BigDecimal amount;

    @Column(name = "created_at", updatable = false)
    @Schema(name = "createdAt", example = "2025-02-07T14:30:00", description = "Дата и время создания транзакции")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
