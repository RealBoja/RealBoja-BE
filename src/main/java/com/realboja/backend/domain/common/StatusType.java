package com.realboja.backend.domain.common;

import lombok.Getter;

@Getter
public enum StatusType {

	COLD_ROOM("냉동방형"),
	WOKE_UP("방 깨우기 성공형"),
	NO_LEADER("총대 실종형"),
	PURPOSE_MATCH("목적 일치형"),
	MEET_SOON("약속 임박형");

	private final String label;

	StatusType(String label) {
		this.label = label;
	}
}
