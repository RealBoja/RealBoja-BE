package com.realboja.backend.domain.common;

import lombok.Getter;

@Getter
public enum RoomType {

	HIGH_SCHOOL("고등학교 친구방"),
	UNIVERSITY("대학교 동기방"),
	CLUB("동아리/모임방"),
	EX_COWORKER("전 직장 동료방"),
	PROJECT_TEAM("프로젝트 끝난 팀방"),
	FAMILY("가족/친척방"),
	OTHER("기타");

	private final String label;

	RoomType(String label) {
		this.label = label;
	}
}
