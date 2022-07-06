## daangn-server

- 당근 서버 리포지토리

---

### 목차

1. [API design](#api-design)
1. [Overall structure design](#overall-structure-design)
    - [daangn-server architecture](#daangn-server-architecture)
1. [DB schema design](#db-schema-design)
1. [사용 기술](#사용-기술)

---

### API design

- [API 명세서](https://github.com/daangn-daangn/daangn-server/wiki/API-%EB%AA%85%EC%84%B8)

---

### Overall structure design

#### daangn-server architecture

- `api-server`: 유저, 물품, 거래 후기, 찜하기 등 프로젝트 전반 API를 담당하는 서버 
- `chat-server`: 채팅 관련 기능을 담당하는 서버
- `notification-server`: api-server로부터 요청을 받아 알림 처리를 담당하는 서버 

![프로젝트 아키텍쳐 generator 007](https://user-images.githubusercontent.com/75410527/177644798-a600770e-87ee-46b2-820c-423c012c3653.jpeg)

---

### DB schema design

- [도메인 테이블 설계(ERDCloud)](https://www.erdcloud.com/d/8JpeniGokjXbkYvZv)

![도메인 테이블 설계](https://user-images.githubusercontent.com/75410527/177047609-9b5723ef-a22d-4191-8d04-69b71b35ef04.png)

---

### 사용 기술

- SpringBoot v2.6.4 (gradle)
- Spring Data Jpa 
- Querydsl
- Apache Kafka
- Docker
- mongodb
- mysql
- AWS S3
