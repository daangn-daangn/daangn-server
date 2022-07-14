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

![daangn-daangn](https://user-images.githubusercontent.com/75410527/178842214-bb7a49a4-20ee-473f-b6be-bd59e1513ee3.jpeg)

#### daangn-server architecture

- `api-server`: 유저, 물품, 거래 후기, 찜하기 등 프로젝트 전반 API를 담당하는 서버 
- `chat-server`: 채팅 관련 기능을 담당하는 서버
- `notification-server`: api-server로부터 요청을 받아 알림 처리를 담당하는 서버 

![daangn-server](https://user-images.githubusercontent.com/75410527/178842230-d88fa5e8-95f7-44e8-ac48-270ef1894f96.jpeg)

---

### DB schema design

- [도메인 테이블 설계(ERDCloud)](https://www.erdcloud.com/d/YdkZH58WZMtdcPY7A)

#### API 관련 ERD

<img width="1401" alt="erd-daangn-server" src="https://user-images.githubusercontent.com/75410527/178994633-37e4131a-5132-43ff-8e4c-112dff49f896.png">

#### 채팅 관련 ERD

<img width="1023" alt="erd-2-daangn-server" src="https://user-images.githubusercontent.com/75410527/178994673-487fda62-3649-4ebf-9127-721d6251393c.png">

---


### 사용 기술

- SpringBoot v2.6.4 (gradle)
- Spring Data Jpa (Querydsl)
- Apache Kafka
- Docker
- mysql
- mongodb
- Spring WebSocket STOMP
- Git-action & AWS CodeDeploy
