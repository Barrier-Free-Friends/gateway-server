# Gateway Server

Barrier Free Friends MSA의 API Gateway입니다. Spring Cloud Gateway(WebFlux) 기반으로 모든 외부 요청의 단일 진입점 역할을 하며, 인증과 라우팅을 담당합니다.

전체 서비스 구성은 [조직 README](https://github.com/Barrier-Free-Friends)를 참고하세요.

## 기술 스택
- Java 21, Spring Boot 3.4.11 (WebFlux)
- Spring Cloud Gateway, Eureka Client, Config Client, LoadBalancer
- Spring Security, JJWT
- Springdoc (WebFlux Swagger UI)

## 주요 기능
- JWT 검증 및 사용자 정보 헤더 주입 (`AddUserInfoFilter`)
- Eureka 기반 서비스 라우팅 (라우트 정의는 Config Server에서 동적으로 로드)
- CORS 및 보안 설정
- 통합 API 문서 제공 (`/api-docs.html`)

## 실행

```bash
./gradlew bootRun
```

기본 포트: `3000`

필요 환경변수: `JWT_SECRET`

## Docker

```bash
docker build -t gateway-server .
docker run -p 3000:3000 gateway-server
```
