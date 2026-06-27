package com.realboja.backend.domain.reaction.controller;

import com.realboja.backend.domain.reaction.ReactionService;
import com.realboja.backend.domain.reaction.dto.CreateParticipantRequest;
import com.realboja.backend.domain.reaction.dto.CreateReactionRequest;
import com.realboja.backend.domain.reaction.dto.CreateReactionResponse;
import com.realboja.backend.domain.reaction.dto.ParticipantResponse;
import com.realboja.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Reaction", description = "참여자 반응 API")
@RestController
@RequestMapping("/api/rooms/{roomCode}")
@RequiredArgsConstructor
public class ReactionController {

	private final ReactionService reactionService;

	@Operation(summary = "참가자 등록", description = "약속방에서 사용할 닉네임을 등록하고 participantId를 발급합니다.")
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "참가자 등록 성공")
	})
	@PostMapping("/participants")
	public ResponseEntity<ApiResponse<ParticipantResponse>> createParticipant(
		@PathVariable String roomCode,
		@Valid @RequestBody CreateParticipantRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.success(reactionService.createParticipant(roomCode, request)));
	}

	@Operation(summary = "반응 남기기", description = "participantId 기반으로 약속방에 반응을 남깁니다. 같은 참가자면 기존 반응을 수정합니다.")
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "반응 생성 또는 수정 성공")
	})
	@PostMapping("/reactions")
	public ResponseEntity<ApiResponse<CreateReactionResponse>> createReaction(
		@PathVariable String roomCode,
		@Valid @RequestBody CreateReactionRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.success(reactionService.createReaction(roomCode, request)));
	}

	@Operation(summary = "참가자 확인", description = "localStorage에 저장된 participantId가 해당 방에서 유효한지 확인합니다.")
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "참가자 확인 성공")
	})
	@GetMapping("/participants/{participantId}")
	public ResponseEntity<ApiResponse<ParticipantResponse>> getParticipant(
		@PathVariable String roomCode,
		@PathVariable Long participantId
	) {
		return ResponseEntity.ok(ApiResponse.success(reactionService.getParticipant(roomCode, participantId)));
	}
}
