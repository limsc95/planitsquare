# 📅 Holiday Keeper
전 세계 공휴일 데이터를 외부 API를 통해 수집·조회·관리하는 백엔드 미니 서비스입니다.

---

## 🧩 프로젝트 개요

- **과제명**: 플랜잇스퀘어 백엔드 개발자 채용 과제
- **주제**: 최근 5년간(2020 ~ 2025)의 국가별 공휴일 데이터를 외부 API를 통해 적재/조회/관리
- **개발 기간**: YYYY.MM.DD ~ YYYY.MM.DD (6일 이내)

---

## 외부 API

| 구분 | 내용 | 응답 |
|------|------|------|
| 국가목록 | GET https://date.nager.at/api/v3/AvailableCountries | 국가배열 |
| 특정 연도 공휴일 | GET https://date.nager.at/api/v3/PublicHolidays/{year}/{countryCode} e.g.https://date.nager.at/api/v3/PublicHolidays/2025/KR | 공휴일 |

---

## 🔧 기술 스택

| 구분 | 내용 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 3.5.3 |
| DB | H2 (In-Memory) |
| ORM | Spring Data JPA, Querydsl 5 |
| 문서화 | SpringDoc OpenAPI 3 (Swagger UI) |
| 테스트 | JUnit5 |
| 빌드 | Gradle |
| 기타 | Lombok, Validation, DevTools |

---

## ✅ 기능 명세

| 기능 | 설명 |
|------|------|
| 공휴일 데이터 적재 | 2020~2025년, 모든 국가 공휴일 데이터를 외부 API로부터 수집 및 저장 |
| 공휴일 조회 | 연도/국가 기준 필터, 페이징, from~to/타입 필터 등 확장 가능 |
| 데이터 재동기화 | 특정 국가/연도 기준 공휴일 데이터 upsert (다시 동기화) |
| 데이터 삭제 | 특정 국가/연도 기준 데이터 전체 삭제 |
| (선택) 자동 배치 | 매년 1월 2일 01:00 KST에 전년도/당해년도 데이터 자동 수집 |

---

## 🔗 API 명세 요약

### 📌 공휴일 조회 API
