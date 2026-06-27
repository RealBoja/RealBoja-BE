package com.realboja.backend.domain.card;

import com.realboja.backend.domain.card.dto.CreateCardResponse;
import com.realboja.backend.domain.common.CardType;
import com.realboja.backend.domain.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

    private static final String WAKE_TITLE = "이 방 마지막 만남: 거의 전설";
    private static final String WAKE_BODY = "나중에 보자'만 반복 중\n생존자 3명만 모이면 약속 해동 시작";
    private static final String WAKE_CTA = "진짜 볼 사람?";

    private final CardRepository cardRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public CreateCardResponse createWakeCard(String roomCode) {
        Long roomId = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다: " + roomCode))
                .getRoomId();

        Card card = Card.create(roomId, CardType.WAKE, 1, WAKE_TITLE, WAKE_BODY, WAKE_CTA);
        return CreateCardResponse.from(cardRepository.save(card));
    }
}
