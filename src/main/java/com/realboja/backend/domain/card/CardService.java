package com.realboja.backend.domain.card;

import com.realboja.backend.domain.card.dto.CardContent;
import com.realboja.backend.domain.card.dto.CreateCardResponse;
import com.realboja.backend.domain.common.CardType;
import com.realboja.backend.domain.room.Room;
import com.realboja.backend.domain.room.RoomRepository;
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
    public CreateCardResponse createWakeCard(String roomCode) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다: " + roomCode));

        CardContent content = cardContentGenerator.generate(room);
        Card card = Card.create(room.getRoomId(), CardType.WAKE, 1, content.title(), content.body(), content.ctaText());
        return CreateCardResponse.from(cardRepository.save(card));
    }
}
