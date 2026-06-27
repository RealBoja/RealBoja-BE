package com.realboja.backend.domain.reaction.controller;

import com.realboja.backend.domain.reaction.ReactionService;
import com.realboja.backend.domain.reaction.dto.CreateReactionRequest;
import com.realboja.backend.domain.reaction.dto.CreateReactionResponse;
import com.realboja.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Reaction", description = "참여자 반응 API")
@RestController
@RequestMapping("/api/rooms/{roomCode}/reactions")
@RequiredArgsConstructor
public class ReactionController {

	private final ReactionService reactionService;

	@Operation(summary = "반응 남기기", description = "닉네임 기반으로 약속방에 반응을 남깁니다. 같은 닉네임이면 기존 반응을 수정합니다.")
	@PostMapping
	public ResponseEntity<ApiResponse<CreateReactionResponse>> createReaction(
		@PathVariable String roomCode,
		@Valid @RequestBody CreateReactionRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.success(reactionService.createReaction(roomCode, request)));
	}
}
