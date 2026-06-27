package com.realboja.backend.domain.common;

import lombok.Getter;

@Getter
public enum ReactionType {

	REALLY_MEET("나 진짜 볼래"),
	PURPOSE_OK("목적이면 감"),
	IF_SOMEONE_LEADS("누가 잡으면 감"),
	JUST_ALIVE("일단 생존신고");

	private final String label;

	ReactionType(String label) {
		this.label = label;
	}
}
