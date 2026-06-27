package com.realboja.backend.domain.schedule;

import com.realboja.backend.domain.room.Room;
import com.realboja.backend.domain.room.RoomRepository;
import com.realboja.backend.domain.schedule.dto.ScheduleResultResponse;
import com.realboja.backend.domain.schedule.dto.ScheduleResultResponse.PlaceRecommendation;
import com.realboja.backend.domain.schedule.dto.ScheduleResultResponse.PlaceRecommendationGuide;
import com.realboja.backend.domain.schedule.dto.ScheduleResultResponse.TimeSlotResult;
import com.realboja.backend.domain.schedule.dto.SubmitScheduleRequest;
import com.realboja.backend.domain.schedule.dto.SubmitScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final RoomRepository roomRepository;
    private final ScheduleVoteRepository scheduleVoteRepository;
    private final PlaceRecommendationService placeRecommendationService;

    @Transactional
    public SubmitScheduleResponse submitSchedule(String roomCode, SubmitScheduleRequest request) {
        Room room = findRoom(roomCode);

        // 같은 닉네임이 다시 제출하면 기존 선택을 모두 지우고 새로 저장 (중복 선택 = 여러 행)
        scheduleVoteRepository.deleteByRoomAndNickname(room, request.nickname());

        // 같은 시간대를 중복으로 보내도 한 번만 저장
        List<TimeSlot> distinctSlots = request.timeSlots().stream().distinct().toList();

        List<ScheduleVote> votes = distinctSlots.stream()
                .map(slot -> ScheduleVote.builder()
                        .room(room)
                        .nickname(request.nickname())
                        .location(request.location())
                        .timeSlot(slot)
                        .build())
                .toList();

        scheduleVoteRepository.saveAll(votes);
        return SubmitScheduleResponse.of(room.getRoomCode(), request.nickname(), request.location(), distinctSlots);
    }

    @Transactional(readOnly = true)
    public ScheduleResultResponse getResult(String roomCode) {
        Room room = findRoom(roomCode);
        List<ScheduleVote> votes = scheduleVoteRepository.findAllByRoom(room);

        // 시간대별 선택한 닉네임 모으기 (선택 안 된 시간대도 0으로 표시)
        List<TimeSlotResult> results = java.util.Arrays.stream(TimeSlot.values())
                .map(slot -> {
                    List<String> nicknames = votes.stream()
                            .filter(v -> v.getTimeSlot() == slot)
                            .map(ScheduleVote::getNickname)
                            .toList();
                    return new TimeSlotResult(slot, slot.getLabel(), nicknames.size(), nicknames);
                })
                .sorted(Comparator.comparingInt(TimeSlotResult::count).reversed())
                .toList();

        // 참여 인원 = 한 번이라도 투표한 고유 닉네임 수
        Set<String> participants = votes.stream()
                .map(ScheduleVote::getNickname)
                .collect(Collectors.toSet());

        // 최다 선택 시간대 (아무도 안 골랐으면 null)
        TimeSlotResult topTimeSlot = results.stream()
                .filter(r -> r.count() > 0)
                .findFirst()
                .orElse(null);

        Map<String, String> participantLocations = votes.stream()
                .filter(v -> v.getLocation() != null && !v.getLocation().isBlank())
                .collect(Collectors.toMap(
                        ScheduleVote::getNickname,
                        ScheduleVote::getLocation,
                        (existing, replacement) -> replacement,
                        LinkedHashMap::new
                ));

        List<PlaceRecommendation> placeRecommendations = placeRecommendationService.recommend(
                participantLocations,
                topTimeSlot == null ? null : topTimeSlot.timeSlot()
        );

        PlaceRecommendationGuide placeRecommendationGuide = buildPlaceRecommendationGuide(participantLocations.size());

        return new ScheduleResultResponse(
                participants.size(),
                topTimeSlot,
                results,
                placeRecommendationGuide,
                placeRecommendations
        );
    }

    private PlaceRecommendationGuide buildPlaceRecommendationGuide(int participantLocationCount) {
        if (participantLocationCount == 0) {
            return new PlaceRecommendationGuide(
                    "EMPTY",
                    "추천 만남 후보",
                    "아직 시간대와 출발지를 남긴 사람이 없어요."
            );
        }

        if (participantLocationCount == 1) {
            return new PlaceRecommendationGuide(
                    "READY",
                    "추천 만남 후보",
                    "입력한 출발지를 기준으로 추천했어요."
            );
        }

        return new PlaceRecommendationGuide(
                "READY",
                "추천 만남 후보",
                "출발지, 이동 편의성, 만남 목적을 참고해 추천했어요."
        );
    }

    private Room findRoom(String roomCode) {
        return roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다: " + roomCode));
    }
}
