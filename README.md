# **To-Do 웹 애플리케이션 백엔드**


---
### **📖프로젝트 개요 및 설명**
- 기본적인 CRUD 기능을 제공하는 To-Do 웹 애플리케이션의 백엔드입니다.
- Spring Boot를 기반으로 REST API를 제공하며, 사용자는 할 일 생성, 조회, 수정, 삭제할 수 있습니다.
- JWT 기반 인증을 구현하였으며, Swagger를 활용하여 API 문서화도 진행하였습니다.
- 이 프로젝트는 기본적인 CRUD 기능에 JWT, Swagger, 배포, 테스팅 등 다양한 기술을 적용해 보며 공부하는 것을 목표로 합니다.


---
### **🛠️기술 스택**
|기술|버전|
|---|---|
|**Java**|17|
|**Spring Boot**|3.3.5|
|**Spring Security**|6.4.3|
|**JWT (JSON Web Token)**|0.11.5|
|**MySQL**|8.0.26|
|**JPA (Hibernate)**|6.6.8|
|**Swagger**|2.0.2|


---
### **📂디렉토리 구조**

```
───src
    ├───main
    │   ├───java.org.example.todo
    │   │   ├───config				# 설정 파일
    │   │   │   └───jwt				# JWT 관련 설정
    │   │   ├───controller			# API 요청 처리
    │   │   │   ├───schedule
    │   │   │   ├───token
    │   │   │   └───user
    │   │   ├───domain				# Entity 파일
    │   │   ├───dto					# Dto 파일 
    │   │   │   ├───mapper
    │   │   │   ├───schedule
    │   │   │   ├───token
    │   │   │   └───user
    │   │   ├───exception			# 예외처리
    │   │   ├───repository			# 데이터베이스 접근(JPA)
    │   │   └───service				# 비즈니스 로직 구현
    │   │        ├───schedule
    │   │        ├───token
    │   │        └───user
    │   └───resources				# 페이지 html 파일
    │       ├───static
    │       └───templates

```

---
## 📌**주요 기능**

#### 사용자 인증 
- **회원가입 및 로그인**
	- User 추가
	- User 조회
	- User 정보 수정
	- User 정보 삭제
- **JWT를 이용한 인증**

#### To-Do 관리 기능
- **할 일 추가**: 새로운 To-Do 항목을 생성
- **할 일 조회**: 사용자의 To-Do 항목/목록 조회
- **할 일 수정**: 기존 To-Do의 내용 및 상태 변경
- **할 일 삭제**: To-Do 항목을 삭제

#### API 문서화
- Swagger를 통해 **API 문서화**
- 기본 URL: `http://localhost:8080/swagger-ui/index.html`



---
### **📃API 명세서**

[API 명세서 notion](https://positive-woolen-1a2.notion.site/API-1ee8835a509081b48508e4695be8770f?source=copy_link)




