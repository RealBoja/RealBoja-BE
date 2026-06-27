package com.realboja.backend.domain.common;

import lombok.Getter;

@Getter
public enum Purpose {

	MEAL("밥"),
	CAFE("카페"),
	DRINK("술"),
	JUST_SEE("그냥 얼굴 보기");

	private final String label;

	Purpose(String label) {
		this.label = label;
	}
}
