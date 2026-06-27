package com.realboja.backend.domain.schedule;

import com.realboja.backend.domain.schedule.dto.ScheduleResultResponse;
import com.realboja.backend.domain.schedule.dto.SubmitScheduleRequest;
import com.realboja.backend.domain.schedule.dto.SubmitScheduleResponse;
import com.realboja.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Schedule", description = "시간대 선택/결과 API")
@RestController
@RequestMapping("/api/rooms/{roomCode}/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "내 시간대 남기기", description = "참여자가 가능한 시간대를 선택해 저장합니다. 같은 닉네임이면 기존 선택을 덮어씁니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<SubmitScheduleResponse>> submitSchedule(
            @PathVariable String roomCode,
            @Valid @RequestBody SubmitScheduleRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(scheduleService.submitSchedule(roomCode, request)));
    }

    @Operation(summary = "시간대 결과 조회", description = "시간대별 선택 인원과 최다 선택 시간대를 반환합니다.")
    @GetMapping
    public ApiResponse<ScheduleResultResponse> getResult(@PathVariable String roomCode) {
        return ApiResponse.success(scheduleService.getResult(roomCode));
    }
}
