package com.realboja.backend.domain.reaction.controller;

import com.realboja.backend.domain.common.ReactionType;
import com.realboja.backend.domain.reaction.dto.CreateReactionRequest;
import com.realboja.backend.domain.reaction.dto.CreateReactionResponse;
import com.realboja.backend.global.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms/{roomCode}/reactions")
public class ReactionController {

	@PostMapping
	public ResponseEntity<ApiResponse<CreateReactionResponse>> createReaction(
		@PathVariable String roomCode,
		@Valid @RequestBody CreateReactionRequest request
	) {
		CreateReactionResponse response = new CreateReactionResponse(
			1L,
			request.nickname(),
			request.reactionType() == null ? ReactionType.REALLY_MEET : request.reactionType()
		);

		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
	}
}
