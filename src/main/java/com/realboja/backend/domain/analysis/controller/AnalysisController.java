package com.realboja.backend.domain.analysis.controller;

import com.realboja.backend.domain.analysis.AnalysisService;
import com.realboja.backend.domain.analysis.dto.AnalysisResponse;
import com.realboja.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Analysis", description = "방 분석 API")
@RestController
@RequestMapping("/api/rooms/{roomCode}/analysis")
@RequiredArgsConstructor
public class AnalysisController {

	private final AnalysisService analysisService;

	@Operation(summary = "분석 조회", description = "방의 반응 현황을 기반으로 약속 온도와 상태를 계산합니다.")
	@GetMapping
	public ResponseEntity<ApiResponse<AnalysisResponse>> getAnalysis(
		@PathVariable String roomCode
	) {
		return ResponseEntity.ok(ApiResponse.success(analysisService.getAnalysis(roomCode)));
	}
}
