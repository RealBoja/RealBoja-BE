package com.realboja.backend.global.health;

import java.time.Instant;
import java.util.Map;
import com.realboja.backend.global.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

	@GetMapping("/api/health")
	public ApiResponse<Map<String, Object>> health() {
		return ApiResponse.success(Map.of(
			"status", "ok",
			"service", "realboja-backend",
			"timestamp", Instant.now().toString()
		));
	}
}
