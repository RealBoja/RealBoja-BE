package com.realboja.backend.domain.card;

import com.realboja.backend.domain.card.dto.CreateCardResponse;
import com.realboja.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Card", description = "카드 생성 API")
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @Operation(summary = "WAKE 카드 생성", description = "roomCode로 방을 찾아 WAKE 카드를 생성합니다.")
    @PostMapping("/{roomCode}/card")
    public ResponseEntity<ApiResponse<CreateCardResponse>> createWakeCard(@PathVariable String roomCode) {
        CreateCardResponse response = cardService.createWakeCard(roomCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }
}
