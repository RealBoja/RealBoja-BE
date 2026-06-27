package com.realboja.backend.domain.analysis.controller;

import com.realboja.backend.domain.common.ReactionType;
import com.realboja.backend.domain.common.StatusType;
import com.realboja.backend.domain.analysis.dto.AnalysisResponse;
import com.realboja.backend.global.response.ApiResponse;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms/{roomCode}/analysis")
public class AnalysisController {

	@GetMapping
	public ResponseEntity<ApiResponse<AnalysisResponse>> getAnalysis(
		@PathVariable String roomCode
	) {
		AnalysisResponse response = new AnalysisResponse(
			0,
			StatusType.COLD_ROOM,
			StatusType.COLD_ROOM.getLabel(),
			0,
			0,
			0.0,
			Map.of(
				ReactionType.REALLY_MEET, 0,
				ReactionType.PURPOSE_OK, 0,
				ReactionType.IF_SOMEONE_LEADS, 0,
				ReactionType.JUST_ALIVE, 0
			),
			"아직 반응이 부족해요.",
			"먼저 생존신고를 받아보세요."
		);

		return ResponseEntity.ok(ApiResponse.success(response));
	}
}
