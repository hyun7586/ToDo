### This is simple ToDo web application 

# 📋 ToDo 웹 애플리케이션 백엔드

> ToDo 관리 애플리케이션의 백엔드 repo임. Spring Boot 기반으로 RESTful API를 제공하며, 사용자가 일정 관리와 인증을 쉽게 수행할 수 있도록 설계함.

---

## 📑 프로젝트 소개

이 프로젝트는 다음과 같은 목적을 가지고 개발되었다.
- 사용자 인증 및 권한 관리(JWT 및 OAuth2 지원).
- 일정을 생성, 수정, 삭제 및 조회하는 CRUD 기능 제공.
- RESTful API 설계와 데이터 검증 및 예외 처리를 지원.
- 효율적인 데이터 처리 및 확장 가능한 구조 설계.

---

## 🛠 기능

- **인증 및 권한 관리**:
  - JWT 기반 로그인/회원가입
  - Google OAuth2 지원
- **일정 관리**:
  - 일정 생성, 수정, 삭제, 조회 (CRUD)
  - 일정 필터링 및 정렬 지원
- **API 데이터 검증**:
  - 요청 데이터에 대한 Validation
  - 통일된 에러 메시지 제공
- **구조화된 설계**:
  - 계층형 아키텍처로 유지보수 용이
  - DTO와 도메인 모델 분리

---

## 📚 API 명세

### 인증 API
| HTTP 메서드 | 경로               | 설명           |
|-------------|--------------------|----------------|
| POST        | /auth/signup       | 회원가입       |
| POST        | /auth/login        | 로그인         |
| GET         | /auth/oauth/callback | OAuth 인증    |

### 일정 API
| HTTP 메서드 | 경로               | 설명              |
|-------------|--------------------|-------------------|
| POST        | /todos             | 일정 생성         |
| GET         | /todos             | 전체 일정 조회    |
| GET         | /todos/{id}        | 특정 일정 조회    |
| PUT         | /todos/{id}        | 일정 수정         |
| DELETE      | /todos/{id}        | 일정 삭제         |

---

## 🏗️ 구조 및 설계

### 프로젝트 구조
```plaintext
src/
├── main/
│   ├── java/
│   │   ├── com.example.todo/
│   │   │   ├── config/         # 설정 클래스
│   │   │   ├── controller/     # API 컨트롤러
│   │   │   ├── domain/         # 도메인 엔티티
│   │   │   ├── dto/            # DTO 클래스
│   │   │   ├── repository/     # JPA 레포지토리
│   │   │   ├── service/        # 비즈니스 로직
│   │   │   └── exception/      # 예외 처리
│   └── resources/
│       ├── application.yml     # 환경 설정 파일
├── test/                       # 테스트 코드
