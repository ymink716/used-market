package com.ymink716.used_market.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
    name = "trade_requests",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "unique_item_requester",
            columnNames = {"item_id", "requester_id"}
        )
    }
)
public class TradeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeRequestStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public TradeRequest(Item item, User requester) {
        this.item = item;
        this.requester = requester;
        this.status = TradeRequestStatus.PENDING;
    }

    public void accept() {
        this.status = TradeRequestStatus.ACCEPTED;
    }

    public void reject() {
        this.status = TradeRequestStatus.REJECTED;
    }
}