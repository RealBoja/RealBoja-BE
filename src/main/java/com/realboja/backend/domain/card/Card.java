package com.realboja.backend.domain.card;

import com.realboja.backend.domain.common.CardType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long roomId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CardType cardType;

    @Column(nullable = false)
    private int stepOrder;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(nullable = false)
    private String ctaText;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Card() {}

    private Card(Long roomId, CardType cardType, int stepOrder, String title, String body, String ctaText) {
        this.roomId = roomId;
        this.cardType = cardType;
        this.stepOrder = stepOrder;
        this.title = title;
        this.body = body;
        this.ctaText = ctaText;
    }

    public static Card create(Long roomId, CardType cardType, int stepOrder, String title, String body, String ctaText) {
        return new Card(roomId, cardType, stepOrder, title, body, ctaText);
    }

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
