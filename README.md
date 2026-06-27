# 진짜보자 Backend

진짜보자 Spring Boot 백엔드 레포지토리입니다.

진짜보자는 조용해진 단톡방에 AI 약속 카드를 공유하고, 참여자 반응을 기반으로 방의 약속 상태를 분석해 실제 만남으로 이어주는 약속 재점화 서비스입니다.

## 역할

- 닉네임 기반 익명 참여 (해커톤/MVP용, 로그인 없음)
- 약속방 생성 및 조회 API
- AI 깨우기 카드 생성 엔드포인트
- 참여자 반응 수집 API
- 약속 온도 및 방 지표 분석 API
- 방 상태 분석 및 다음 카드 추천 API
- 2차 카드(메뉴/시간대/가벼운 만남) 생성 API

## 관련 레포지토리

- Frontend: https://github.com/RealBoja/RealBoja-FE
- Docs hub: https://github.com/RealBoja/RealBoja

## 추천 구조

```
JinjjaBoja-Backend/
├─ src/main/java/com/jinjjaboja/backend/
├─ src/main/resources/
├─ docs/
└─ .github/
```

## MVP 도메인

- `Room`: 약속방 (방 유형, 인원수, 목적, 톤, 진행 단계)
- `Card`: 카드 (깨우기/결과/메뉴/시간대/가벼운 만남/부스터)
- `Reaction`: 닉네임 기반 참여자 반응
- `Analysis`: 약속 온도, 방 지표, 방 상태, 다음 카드 추천
- `Notification`: 반응 수집, 결과 카드 공유 알림 (P1)

## 브랜치 컨벤션

```
feat/{feature-name}
fix/{bug-name}
chore/{task-name}
docs/{doc-name}
```

| 접두사 | 언제 쓰나 |
| --- | --- |
| `feat/` | 새 기능/API 작업 브랜치 (예: `feat/wake-card-generation`) |
| `fix/` | 버그 수정 브랜치 (예: `fix/participation-rate`) |
| `chore/` | 빌드/설정/의존성 등 코드 외 작업 (예: `chore/spring-setup`) |
| `docs/` | 문서 작업 브랜치 (예: `docs/api-contract`) |

## 커밋 컨벤션

```
feat: add wake card generation api
fix: handle zero participant rate
chore: configure spring boot project
docs: update API contract
```

| 타입 | 언제 쓰나 |
| --- | --- |
| `feat` | 새 기능이나 API를 추가할 때 (약속방 생성, 반응 수집 등) |
| `fix` | 버그를 고칠 때 (참여율 0명 NaN, 잘못된 온도 계산 등) |
| `refactor` | 동작은 그대로 두고 코드 구조만 개선할 때 (프롬프트 빌더 분리 등) |
| `test` | 테스트 코드를 추가/수정할 때 (온도 계산 단위 테스트 등) |
| `chore` | 기능과 무관한 설정·빌드·의존성 작업일 때 (Spring 설정, 패키지 추가) |
| `docs` | 문서만 바꿀 때 (README, API 명세 등) |
| `perf` | 성능을 개선할 때 (쿼리 최적화 등) |

> 규칙: subject는 50자 이내, 마침표 없이, "무엇을 했다" 위주로 명확하게 작성합니다.
>
