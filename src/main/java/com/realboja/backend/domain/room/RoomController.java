package com.realboja.backend.domain.room;

import com.realboja.backend.domain.card.CardService;
import com.realboja.backend.domain.card.dto.CreateCardResponse;
import com.realboja.backend.domain.room.dto.CreateRoomRequest;
import com.realboja.backend.domain.room.dto.CreateRoomResponse;
import com.realboja.backend.domain.room.dto.RoomDetailResponse;
import com.realboja.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Room", description = "방 생성/조회 API")
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final CardService cardService;

    @Operation(summary = "방 생성", description = "방을 만들고 roomCode를 발급합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<CreateRoomResponse>> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(roomService.createRoom(request)));
    }

    @Operation(summary = "방 조회", description = "roomCode로 방 정보를 조회합니다.")
    @GetMapping("/{roomCode}")
    public ApiResponse<RoomDetailResponse> getRoom(@PathVariable String roomCode) {
        return ApiResponse.success(roomService.getRoom(roomCode));
    }

    @Operation(summary = "일정 조율 단계로 전환", description = "과반수 이상이 반응한 경우 방장이 일정 조율 단계로 수동 전환하고 Schedule 카드를 생성합니다.")
    @PostMapping("/{roomCode}/advance")
    public ResponseEntity<ApiResponse<CreateCardResponse>> advanceToScheduling(@PathVariable String roomCode) {
        roomService.advanceToScheduling(roomCode);
        CreateCardResponse card = cardService.createCard(roomCode);
        return ResponseEntity.ok(ApiResponse.success(card));
    }
}
