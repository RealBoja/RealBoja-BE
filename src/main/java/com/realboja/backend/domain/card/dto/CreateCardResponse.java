package com.realboja.backend.domain.card.dto;

import com.realboja.backend.domain.card.Card;

public record CreateCardResponse(
        Long cardId,
        Long roomId,
        String cardType,
        String title,
        String body,
        String ctaText
) {
    public static CreateCardResponse from(Card card) {
        return new CreateCardResponse(
                card.getId(),
                card.getRoomId(),
                card.getCardType().name(),
                card.getTitle(),
                card.getBody(),
                card.getCtaText()
        );
    }
}
