package com.realboja.backend.domain.room;

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
