package com.realboja.backend.domain.room;

import com.realboja.backend.domain.room.dto.CreateRoomRequest;
import com.realboja.backend.domain.room.dto.CreateRoomResponse;
import com.realboja.backend.domain.room.dto.RoomDetailResponse;
import com.realboja.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Room", description = "방 생성/조회 API")
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "방 생성", description = "방을 만들고 roomCode를 발급합니다.")
    @PostMapping
    public ApiResponse<CreateRoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        return ApiResponse.success(roomService.createRoom(request));
    }

    @Operation(summary = "방 조회", description = "roomCode로 방 정보를 조회합니다.")
    @GetMapping("/{roomCode}")
    public ApiResponse<RoomDetailResponse> getRoom(@PathVariable String roomCode) {
        return ApiResponse.success(roomService.getRoom(roomCode));
    }
}
