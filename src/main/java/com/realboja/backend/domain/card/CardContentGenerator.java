package com.realboja.backend.domain.card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realboja.backend.domain.card.dto.CardContent;
import com.realboja.backend.domain.room.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardContentGenerator {

    private final ObjectMapper objectMapper;

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.model:gemini-2.5-flash}")
    private String model;

    public CardContent generate(Room room) {
        String prompt = buildPrompt(room);
        String responseText = callGemini(prompt);
        return parseContent(responseText);
    }

    private String buildPrompt(Room room) {
        return String.format("""
                당신은 '진짜보자' 앱의 WAKE 카드 문구 생성기입니다.
                오랫동안 연락이 뜸했던 친구 방을 다시 깨우는 카드를 만들어 주세요.

                방 정보:
                - 방 유형: %s
                - 마지막 만남: %s
                - 방 인원: %d명
                - 목적: %s
                - 톤: %s

                다음 JSON 형식으로만 응답해 주세요. 다른 텍스트는 절대 포함하지 마세요:
                {
                  "title": "배지 텍스트 — 마지막 만남 기준의 짧은 한 줄 (예: 이 방 마지막 만남: 1달째 잠수)",
                  "body": "카드 본문 — 친구들을 자극하는 2~3줄 메시지 (줄바꿈은 \\\\n 사용)",
                  "ctaText": "버튼 텍스트 — 짧고 행동을 유도하는 한 줄"
                }
                """,
                room.getRoomType().getLabel(),
                room.getLastMeeting().getLabel(),
                room.getRoomSize(),
                room.getPurpose().getLabel(),
                room.getTone().getLabel()
        );
    }

    private String callGemini(String userMessage) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;

        GeminiRequest request = new GeminiRequest(
                List.of(new Content(List.of(new Part(userMessage)))),
                new GenerationConfig(8192, "application/json")
        );

        GeminiResponse response = RestClient.create().post()
                .uri(url)
                .header("Content-Type", "application/json")
                .body(request)
                .retrieve()
                .body(GeminiResponse.class);

        if (response == null || response.candidates().isEmpty()) {
            throw new IllegalStateException("Gemini API 응답이 비어 있습니다.");
        }

        return response.candidates().get(0).content().parts().get(0).text();
    }

    private CardContent parseContent(String json) {
        // Gemini가 ```json ... ``` 블록으로 감쌀 경우 제거
        String cleaned = json.strip();
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replaceAll("(?s)^```[a-z]*\\n?", "").replaceAll("```$", "").strip();
        }
        try {
            return objectMapper.readValue(cleaned, CardContent.class);
        } catch (Exception e) {
            throw new IllegalStateException("AI 응답 파싱 실패: " + json, e);
        }
    }

    record GeminiRequest(List<Content> contents, GenerationConfig generationConfig) {}
    record Content(List<Part> parts) {}
    record Part(String text) {}
    record GenerationConfig(int maxOutputTokens, String responseMimeType) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record GeminiResponse(List<Candidate> candidates) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Candidate(Content content) {}
}
