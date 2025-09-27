package com.example.bankcards.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "card_block_requests")
@Schema(description = "Сущность запроса на блокировку карты")
public class CardBlockRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false, updatable = false)
    @Schema(name = "requestId", example = "1", description = "Уникальный идентификатор запроса")
    private Long requestId;

    @NotNull(message = "Карта обязательна")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false, foreignKey = @ForeignKey(name = "fk_requests_cards"))
    @Schema(name = "card", description = "Карта, на которую создается запрос")
    private Card card;

    @NotNull(message = "Пользователь обязателен")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_requests_users"))
    @Schema(name = "user", description = "Пользователь, создавший запрос")
    private User user;

    @Column(name = "status", length = 50)
    @Schema(name = "status", example = "PENDING", description = "Статус запроса")
    private String status;

    @Column(name = "created_at", updatable = false)
    @Schema(name = "createdAt", example = "2025-02-07T14:30:00", description = "Дата и время создания запроса")
    private LocalDateTime createdAt;

    @Column(name = "processed_at")
    @Schema(name = "processedAt", example = "2025-02-08T10:00:00", description = "Дата и время обработки запроса")
    private LocalDateTime processedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by", foreignKey = @ForeignKey(name = "fk_requests_processed_by"))
    @Schema(name = "processedBy", description = "Пользователь, обработавший запрос")
    private User processedBy;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.processedAt = LocalDateTime.now();
    }
}
