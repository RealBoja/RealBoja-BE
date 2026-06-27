package com.realboja.backend.domain.card.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CardContent(String title, String body, String ctaText) {}
