package com.realboja.backend.domain.card;

import com.realboja.backend.domain.card.dto.CreateCardResponse;
import com.realboja.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Card", description = "카드 API")
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @Operation(summary = "카드 생성", description = "방의 현재 단계에 따라 Wake 또는 Schedule 카드를 생성합니다. 이미 존재하면 기존 카드를 반환합니다.")
    @PostMapping("/{roomCode}/card")
    public ResponseEntity<ApiResponse<CreateCardResponse>> createCard(@PathVariable String roomCode) {
        CreateCardResponse response = cardService.createCard(roomCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @Operation(summary = "카드 조회", description = "방의 현재 단계에 해당하는 활성 카드를 조회합니다.")
    @GetMapping("/{roomCode}/card")
    public ResponseEntity<ApiResponse<CreateCardResponse>> getCard(@PathVariable String roomCode) {
        CreateCardResponse response = cardService.getCard(roomCode);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
