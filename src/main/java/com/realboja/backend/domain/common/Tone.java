package com.realboja.backend.domain.common;

import lombok.Getter;

@Getter
public enum Tone {

	PLAIN("담백하게"),
	FUNNY("웃기게"),
	ANNOYING("킹받게"),
	EMOTIONAL("감성적으로");

	private final String label;

	Tone(String label) {
		this.label = label;
	}
}
