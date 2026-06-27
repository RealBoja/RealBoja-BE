package com.realboja.backend.domain.card;

import com.realboja.backend.domain.card.dto.CardContent;
import com.realboja.backend.domain.card.dto.CreateCardResponse;
import com.realboja.backend.domain.common.CardType;
import com.realboja.backend.domain.room.Room;
import com.realboja.backend.domain.room.RoomRepository;
import com.realboja.backend.domain.room.RoomStep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final RoomRepository roomRepository;
    private final CardContentGenerator cardContentGenerator;

    @Transactional
    public CreateCardResponse createCard(String roomCode) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다: " + roomCode));

        CardType cardType = room.getCurrentStep() == RoomStep.SCHEDULING ? CardType.SCHEDULE : CardType.WAKE;

        return cardRepository.findByRoomIdAndCardType(room.getRoomId(), cardType)
                .map(CreateCardResponse::from)
                .orElseGet(() -> {
                    Card card;
                    if (cardType == CardType.SCHEDULE) {
                        card = Card.create(room.getRoomId(), cardType, cardType.getStepOrder(), "", "", "");
                    } else {
                        CardContent content = cardContentGenerator.generate(room, cardType);
                        card = Card.create(room.getRoomId(), cardType, cardType.getStepOrder(), content.title(), content.body(), content.ctaText());
                    }
                    return CreateCardResponse.from(cardRepository.save(card));
                });
    }

    @Transactional(readOnly = true)
    public CreateCardResponse getCard(String roomCode) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다: " + roomCode));

        CardType cardType = room.getCurrentStep() == RoomStep.SCHEDULING ? CardType.SCHEDULE : CardType.WAKE;

        return cardRepository.findByRoomIdAndCardType(room.getRoomId(), cardType)
                .map(CreateCardResponse::from)
                .orElseThrow(() -> new IllegalStateException("아직 생성된 카드가 없습니다."));
    }
}
