## daangn-server

- 당근 서버 리포지토리

---

### 목차

1. [API design](#api-design)
1. [Overall structure design](#overall-structure-design)
    - [daangn-daangn architecture](#daangn-daangn-architecture)
    - [daangn-server architecture](#daangn-server-architecture)
1. [DB schema design](#db-schema-design)
    - [API 관련 ERD](#api-관련-erd)
    - [채팅 관련 ERD](#채팅-관련-erd)
1. [사용 기술](#사용-기술)

---

### API design

- [API 명세서](https://github.com/daangn-daangn/daangn-server/wiki/API-%EB%AA%85%EC%84%B8)

---

### Overall structure design

#### daangn-daangn architecture

![프로젝트 아키텍쳐 generator 002](https://user-images.githubusercontent.com/75410527/181295719-73f15db1-f0bc-4c94-9c8e-9a540e8ba3f3.jpeg)

#### daangn-server architecture

- `api-server`: 유저, 물품, 거래 후기, 찜하기 등 프로젝트 전반 API를 담당하는 서버 
- `chat-server`: 채팅 관련 기능을 담당하는 서버
- `notification-server`: api-server로부터 요청을 받아 알림 처리를 담당하는 서버 

![프로젝트 아키텍쳐 generator 001](https://user-images.githubusercontent.com/75410527/181295759-501508d4-9696-4101-8b03-693012e5e323.jpeg)

---

### DB schema design

- [도메인 테이블 설계(ERDCloud)](https://www.erdcloud.com/d/8P5EmGHF6B7oXYHQx)

#### API 관련 ERD

<img width="1405" alt="erd_diagram_01" src="https://user-images.githubusercontent.com/75410527/181298802-6d8ba8f7-7342-4bc2-9af6-9d2d80d6d34e.png">


#### 채팅 관련 ERD

<img width="1384" alt="erd_diagram_02" src="https://user-images.githubusercontent.com/75410527/181298827-37188ffa-b5cf-4ef8-aa0d-fb75aaaed19a.png">

---


### 사용 기술

- SpringBoot v2.6.4 (gradle)
- Spring Data Jpa (Querydsl)
- Mysql
- Mongodb
- Spring WebSocket STOMP 
- Google guava 
- Apache Kafka 
- Docker & Git-action & AWS CodeDeploy
