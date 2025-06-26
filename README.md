# 📅 Holiday Keeper
전 세계 공휴일 데이터를 외부 API를 통해 수집·조회·관리하는 백엔드 미니 서비스입니다.

---

## 🧩 프로젝트 개요

- **과제명**: 플랜잇스퀘어 백엔드 개발자 채용 과제
- **주제**: 최근 5년간(2020 ~ 2025)의 국가별 공휴일 데이터를 외부 API를 통해 적재/조회/관리
- **개발 기간**: 2025.06.23 ~ 2025.06.29 (6일 이내)

---

## 외부 API

| 구분 | 내용 | 응답 |
|------|------|------|
| 국가목록 | GET https://date.nager.at/api/v3/AvailableCountries | 국가배열 |
| 특정 연도 공휴일 | GET https://date.nager.at/api/v3/PublicHolidays/{year}/{countryCode}<br>e.g.https://date.nager.at/api/v3/PublicHolidays/2025/KR | 공휴일 |

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
## 📦 빌드 & 실행 방법

```bash
# 1. 빌드
./gradlew clean build

# 2. 실행
./gradlew bootRun
```
기본 실행 환경은 H2 (in-memory DB)이며, Swagger UI는 http://localhost:8080/swagger-ui.html 에서 확인 가능합니다.

---

## 🔗 API 명세 요약

### 📌 공휴일 데이터 초기 적재

* **URL**: `POST /api/holidays/init`
* **설명**: 외부 API를 통해 2020\~2025년의 모든 국가 공휴일 데이터를 초기 적재
* **요청 파라미터**: 없음
* **응답**: `200 OK`
* **비고**: 애플리케이션 실행 후 최초 1회 수동 호출용

---

### 📌 공휴일 조회

* **URL**: `GET /api/holidays`
* **설명**: 공휴일 조건 검색 및 페이징 조회

#### ✅ 요청 파라미터 (Query)

| 이름            | 타입          | 필수 | 설명                         |
| ------------- | ----------- | -- | -------------------------- |
| `countryCode` | `String`    | X  | 국가 코드 (예: KR, US)          |
| `year`        | `Integer`   | X  | 연도 (예: 2025)               |
| `fromDate`    | `LocalDate` | X  | 시작일 (`yyyy-MM-dd`)         |
| `toDate`      | `LocalDate` | X  | 종료일 (`yyyy-MM-dd`)         |
| `types`       | `String`    | X  | 공휴일 타입 (예: Public, Bank 등) |
| `page`        | `int`       | X  | 페이지 번호 (기본값: 0)            |
| `size`        | `int`       | X  | 페이지 크기 (기본값: 10)           |

#### ✅ 응답 예시 (200 OK)

```json
{
  "content": [
    {
      "date": "2025-01-01",
      "localName": "새해",
      "name": "New Year's Day",
      "countryCode": "KR",
      "fixed": false,
      "global": true,
      "types": ["Public"],
      "counties": null,
      "launchYear": null
    }
  ],
  "totalPages": 1,
  "totalElements": 1,
  "size": 10,
  "number": 0
}
```

---

### 📌 공휴일 재동기화

* **URL**: `PUT /api/holidays/refresh`
* **설명**: 특정 국가와 연도에 대해 외부 API로부터 최신 데이터를 다시 받아와 DB에 반영 (추가/수정/삭제 포함)

#### ✅ 요청 파라미터 (Query)

| 이름            | 타입        | 필수 | 설명            |
| ------------- | --------- | -- | ------------- |
| `countryCode` | `String`  | O  | 국가 코드 (예: KR) |
| `year`        | `Integer` | O  | 연도 (예: 2025)  |

* **응답**: `200 OK`

---

### 📌 공휴일 삭제

* **URL**: `DELETE /api/holidays/delete`
* **설명**: 특정 국가와 연도의 공휴일 데이터를 DB에서 모두 삭제

#### ✅ 요청 파라미터 (Query)

| 이름            | 타입        | 필수 | 설명            |
| ------------- | --------- | -- | ------------- |
| `countryCode` | `String`  | O  | 국가 코드 (예: KR) |
| `year`        | `Integer` | O  | 연도 (예: 2025)  |

* **응답**: `204 No Content`

---

### 📘 API 문서 (Swagger/OpenAPI)

* Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---
