# Java Spring Boot 프로젝트

## 개요

이 프로젝트는 Java 11 환경에서 동작하는 Spring Boot 기반의 웹 애플리케이션입니다. RESTful 웹 서비스 제공 및 웹 프론트엔드, 데이터 관리 등 엔터프라이즈 애플리케이션 개발에 적합한 구조로 설계되었습니다.

## 주요 기술 스택

- **Java 11**
- **Spring Boot 2.7.x**
  - spring-boot-starter
  - spring-boot-starter-web
  - spring-boot-starter-data-jpa
  - spring-boot-starter-thymeleaf
  - spring-boot-starter-validation
  - spring-boot-starter-test
- **H2 Database** (개발 및 테스트용 인메모리 데이터베이스)
- **Gradle** (빌드 도구)
- **Spotless** (코드 스타일 자동화 및 포맷터)

## 기본 기능

- RESTful API 기본 구조 제공
- 스프링 데이터 JPA를 활용한 DB 접근 및 ORM 지원
- 타임리프(Thymeleaf)를 이용한 동적 서버 사이드 렌더링
- 입력값 검증을 위한 Validation
- 단위 및 통합 테스트 환경 구비 (JUnit 5)
- H2 내장 데이터베이스로 개발 편의성 강화


## 빌드 및 실행 방법

1. **Java 11 환경이 설치되어 있는지 확인**
2. 터미널에서 아래 명령어 실행:
   ```
   ./gradlew clean bootRun
   ```
   또는 IDE에서 DemoApplication 클래스를 실행

3. **웹 접속**:  
   기본적으로 http://localhost:8080 에서 동작합니다.

## 테스트

테스트는 다음 명령어로 실행할 수 있습니다:


## 코드 스타일 및 정적 분석

- `Spotless`를 통해 Google Java Format 스타일을 적용합니다.
- 코드 저장 또는 빌드 시 자동으로 포맷팅과 불필요한 import 정리 수행.


