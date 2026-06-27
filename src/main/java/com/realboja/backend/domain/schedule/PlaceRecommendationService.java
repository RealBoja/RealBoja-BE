package com.realboja.backend.domain.schedule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realboja.backend.domain.schedule.dto.ScheduleResultResponse.PlaceRecommendation;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceRecommendationService {

    private final ObjectMapper objectMapper;

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.model:gemini-2.5-flash}")
    private String model;

    public List<PlaceRecommendation> recommend(Map<String, String> participantLocations, TimeSlot topTimeSlot) {
        if (participantLocations.isEmpty()) {
            return List.of();
        }

        if (participantLocations.size() == 1) {
            return singleParticipantRecommendations(participantLocations.values().iterator().next());
        }

        try {
            String responseText = callGemini(buildPrompt(participantLocations, topTimeSlot));
            PlaceRecommendationResponse response = parseResponse(responseText);

            if (response.recommendations() == null || response.recommendations().isEmpty()) {
                log.warn("Gemini place recommendation response has no recommendations. responseText={}", responseText);
                return fallbackRecommendations();
            }

            return response.recommendations().stream()
                    .filter(recommendation -> recommendation.area() != null && recommendation.reason() != null)
                    .map(this::normalizeRecommendation)
                    .limit(2)
                    .toList();
        } catch (Exception e) {
            log.warn("Failed to generate Gemini place recommendations. fallback will be used.", e);
            return fallbackRecommendations();
        }
    }

    private String buildPrompt(Map<String, String> participantLocations, TimeSlot topTimeSlot) {
        String participants = participantLocations.entrySet().stream()
                .map(entry -> "- " + entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));

        String selectedTime = topTimeSlot == null ? "아직 최다 선택 시간대 없음" : topTimeSlot.getLabel();

        return """
                너는 한국 약속 장소 추천 도우미입니다.
                참여자들의 출발 지역과 선택된 시간대를 보고 만나기 좋은 추천 지역 2개를 골라 주세요.

                기준:
                - 대중교통 접근성이 좋은 곳
                - 특정 참여자에게 너무 치우치지 않는 곳
                - 식사나 카페 약속을 잡기 쉬운 번화가
                - 실제 교통 시간을 단정하지 말고, 일반적인 접근성 기준으로 추천
                - reason은 35자 이내의 자연스러운 한국어 한 문장
                - hashtags는 화면에 칩으로 보여줄 짧은 해시태그 3개

                참여자 출발 지역:
                %s

                가장 많이 선택된 시간대:
                %s

                반드시 JSON 형식으로만 응답해 주세요. 다른 텍스트는 절대 포함하지 마세요:
                {
                  "recommendations": [
                    {
                      "area": "추천 지역명",
                      "reason": "추천 이유 한 줄",
                      "hashtags": ["#해시태그1", "#해시태그2", "#해시태그3"]
                    },
                    {
                      "area": "추천 지역명",
                      "reason": "추천 이유 한 줄",
                      "hashtags": ["#해시태그1", "#해시태그2", "#해시태그3"]
                    }
                  ]
                }
                """.formatted(participants, selectedTime);
    }

    private String callGemini(String userMessage) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;

        GeminiRequest request = new GeminiRequest(
                List.of(new Content(List.of(new Part(userMessage)))),
                new GenerationConfig(2048, "application/json")
        );

        GeminiResponse response = RestClient.create().post()
                .uri(url)
                .header("Content-Type", "application/json")
                .body(request)
                .retrieve()
                .body(GeminiResponse.class);

        if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
            throw new IllegalStateException("Gemini API 응답이 비어 있습니다.");
        }

        return response.candidates().get(0).content().parts().get(0).text();
    }

    private PlaceRecommendationResponse parseResponse(String json) throws Exception {
        String cleaned = json.strip();
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replaceAll("(?s)^```[a-z]*\\n?", "").replaceAll("```$", "").strip();
        }
        return objectMapper.readValue(cleaned, PlaceRecommendationResponse.class);
    }

    private PlaceRecommendation normalizeRecommendation(PlaceRecommendation recommendation) {
        List<String> hashtags = recommendation.hashtags() == null ? List.of() : recommendation.hashtags().stream()
                .filter(tag -> tag != null && !tag.isBlank())
                .map(String::strip)
                .map(tag -> tag.startsWith("#") ? tag : "#" + tag)
                .limit(3)
                .toList();

        if (hashtags.size() == 3) {
            return new PlaceRecommendation(
                    recommendation.area(),
                    recommendation.reason(),
                    hashtags
            );
        }

        return new PlaceRecommendation(
                recommendation.area(),
                recommendation.reason(),
                List.of("#접근성", "#약속장소", "#만남추천")
        );
    }

    private List<PlaceRecommendation> singleParticipantRecommendations(String location) {
        String baseLocation = location.strip();

        try {
            String responseText = callGemini(buildSingleParticipantPrompt(baseLocation));
            PlaceRecommendationResponse response = parseResponse(responseText);

            if (response.recommendations() == null || response.recommendations().size() < 2) {
                log.warn("Gemini single participant recommendation response is insufficient. responseText={}", responseText);
                return fallbackSingleParticipantRecommendations(baseLocation);
            }

            List<PlaceRecommendation> recommendations = response.recommendations().stream()
                    .filter(recommendation -> recommendation.area() != null && recommendation.reason() != null)
                    .map(this::normalizeRecommendation)
                    .limit(2)
                    .toList();

            if (recommendations.size() < 2) {
                log.warn("Gemini single participant recommendation response has invalid items. responseText={}", responseText);
                return fallbackSingleParticipantRecommendations(baseLocation);
            }

            return recommendations;
        } catch (Exception e) {
            log.warn("Failed to generate Gemini single participant recommendation. fallback will be used.", e);
            return fallbackSingleParticipantRecommendations(baseLocation);
        }
    }

    private String buildSingleParticipantPrompt(String location) {
        return """
                너는 한국 약속 장소 추천 도우미입니다.
                사용자가 입력한 출발지 1개를 기준으로 만남 후보 2개를 추천해 주세요.

                입력 출발지:
                %s

                기준:
                - 첫 번째 추천은 반드시 입력 출발지 자체를 area로 사용
                - 두 번째 추천은 입력 출발지와 실제로 가까운 역이나 주변 번화가를 area로 사용
                - 두 번째 추천이 확실하지 않으면 입력 출발지 주변으로 자연스럽게 표현
                - reason은 35자 이내의 자연스러운 한국어 한 문장
                - hashtags는 화면에 칩으로 보여줄 짧은 해시태그 3개

                반드시 JSON 형식으로만 응답해 주세요. 다른 텍스트는 절대 포함하지 마세요:
                {
                  "recommendations": [
                    {
                      "area": "%s",
                      "reason": "입력 출발지를 추천하는 이유 한 줄",
                      "hashtags": ["#출발역", "#가까움", "#약속장소"]
                    },
                    {
                      "area": "가까운 역 또는 주변 지역명",
                      "reason": "근처 후보로 추천하는 이유 한 줄",
                      "hashtags": ["#근처역", "#이동짧음", "#후보"]
                    }
                  ]
                }
                """.formatted(location, location);
    }

    private List<PlaceRecommendation> fallbackSingleParticipantRecommendations(String baseLocation) {
        String nearbyArea = baseLocation.endsWith("역") ? baseLocation + " 근처" : baseLocation + " 근처역";

        return List.of(
                new PlaceRecommendation(
                        baseLocation,
                        "입력한 출발지라 이동 부담이 가장 적어요.",
                        List.of("#출발역", "#가까움", "#약속장소")
                ),
                new PlaceRecommendation(
                        nearbyArea,
                        "가까운 주변 역에서 가볍게 만나기 좋아요.",
                        List.of("#근처역", "#이동짧음", "#후보")
                )
        );
    }

    private List<PlaceRecommendation> fallbackRecommendations() {
        return List.of(
                new PlaceRecommendation(
                        "강남역",
                        "여러 출발지에서 이동 부담이 비교적 고르게 나와요.",
                        List.of("#접근성", "#늦은시간", "#선택지많음")
                ),
                new PlaceRecommendation(
                        "사당역",
                        "대중교통 선택지가 많아 모이기 편해요.",
                        List.of("#환승편함", "#밥약속", "#중간지점")
                )
        );
    }

    record GeminiRequest(List<Content> contents, GenerationConfig generationConfig) {}
    record Content(List<Part> parts) {}
    record Part(String text) {}
    record GenerationConfig(int maxOutputTokens, String responseMimeType) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record GeminiResponse(List<Candidate> candidates) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Candidate(Content content) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record PlaceRecommendationResponse(List<PlaceRecommendation> recommendations) {}
}
