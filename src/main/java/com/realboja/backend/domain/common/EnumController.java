package com.realboja.backend.domain.common;

import com.realboja.backend.domain.schedule.TimeSlot;
import com.realboja.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Enum", description = "Enum 목록 조회 API")
@RestController
@RequestMapping("/api/enums")
public class EnumController {

    record EnumValue(String value, String label) {}

    @Operation(summary = "Enum 목록 조회", description = "프론트엔드에서 사용하는 모든 Enum 값과 한글 레이블을 반환합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, List<EnumValue>>>> getEnums() {
        Map<String, List<EnumValue>> enums = Map.of(
            "roomType", toEnumValues(RoomType.values(), e -> e.getLabel()),
            "lastMet", toEnumValues(LastMet.values(), e -> e.getLabel()),
            "purpose", toEnumValues(Purpose.values(), e -> e.getLabel()),
            "tone", toEnumValues(Tone.values(), e -> e.getLabel()),
            "reactionType", toEnumValues(ReactionType.values(), e -> e.getLabel()),
            "timeSlot", toEnumValues(TimeSlot.values(), e -> e.getLabel())
        );
        return ResponseEntity.ok(ApiResponse.success(enums));
    }

    private <T extends Enum<T>> List<EnumValue> toEnumValues(T[] values, java.util.function.Function<T, String> labelExtractor) {
        return Arrays.stream(values)
            .map(e -> new EnumValue(e.name(), labelExtractor.apply(e)))
            .toList();
    }
}
