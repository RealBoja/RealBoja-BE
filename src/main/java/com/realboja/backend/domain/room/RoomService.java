package com.realboja.backend.domain.room;

import com.realboja.backend.domain.card.Card;
import com.realboja.backend.domain.card.CardRepository;
import com.realboja.backend.domain.card.dto.CreateCardResponse;
import com.realboja.backend.domain.common.CardType;
import com.realboja.backend.domain.reaction.ReactionRepository;
import com.realboja.backend.domain.room.dto.CreateRoomRequest;
import com.realboja.backend.domain.room.dto.CreateRoomResponse;
import com.realboja.backend.domain.room.dto.RoomDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final ReactionRepository reactionRepository;
    private final CardRepository cardRepository;

    @Transactional
    public CreateRoomResponse createRoom(CreateRoomRequest request) {
        Room room = Room.builder()
                .roomCode(generateUniqueRoomCode())
                .roomType(request.roomType())
                .lastMeeting(request.lastMeeting())
                .roomSize(request.roomSize())
                .purpose(request.purpose())
                .tone(request.tone())
                .build();

        Room saved = roomRepository.save(room);
        return CreateRoomResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public RoomDetailResponse getRoom(String roomCode) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다: " + roomCode));
        return RoomDetailResponse.from(room);
    }

    @Transactional
    public CreateCardResponse advanceToScheduling(String roomCode) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다: " + roomCode));

        if (room.getCurrentStep() == RoomStep.SCHEDULING) {
            throw new IllegalStateException("이미 일정 조율 단계입니다.");
        }

        int reactionCount = reactionRepository.findAllByRoom(room).size();
        if (reactionCount * 2 <= room.getRoomSize()) {
            throw new IllegalStateException("과반수 이상이 반응해야 일정 조율 단계로 넘어갈 수 있습니다.");
        }

        room.advanceToScheduling();

        Card card = Card.create(room.getRoomId(), CardType.SCHEDULE, CardType.SCHEDULE.getStepOrder(), "", "", "");
        return CreateCardResponse.from(cardRepository.save(card));
    }

    private String generateUniqueRoomCode() {
        String code;
        do {
            code = UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0, 6)
                    .toUpperCase();
        } while (roomRepository.existsByRoomCode(code));
        return code;
    }
}
