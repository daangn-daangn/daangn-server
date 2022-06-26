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

![daangn-server-architecture](https://user-images.githubusercontent.com/75410527/175794800-5288cc4f-7ace-49ff-b4e3-a05ddc20c957.jpeg)

---

### DB schema design

- [도메인 테이블 설계](https://www.erdcloud.com/d/P8RnfxxgZsHJvhJDb)

---

### 사용 기술

- SpringBoot v2.6.4 (gradle)
- Kafka
- Docker
- mongodb
- mysql
- AWS S3
