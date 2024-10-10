# GIBBLE - BE

## 역할
- 팀장 : 이태훈
- P&D : 박경민
- Backend : 이태훈, 박대영
- Frontend : 권예원, 구준혁, 허기영

## 기능

### 헌혈증 기부 요청글 등록 및 헌혈증 기부
![image](https://github.com/user-attachments/assets/e586180d-c2c2-4d72-b114-9a4f53f70c67)

### 기부내역 공개

![image](https://github.com/user-attachments/assets/ab0f5a10-cb3a-4844-9e9f-aed8a56e80b9)

### 사용처 및 후기 공개

![image](https://github.com/user-attachments/assets/93488253-c23a-405b-91c5-daebaa40d3bb)

### 다양한 이벤트 참여 가능

![image](https://github.com/user-attachments/assets/35876c64-ff15-438f-b0c7-ed10f79c1b36)

## 시스템 아키텍쳐

![image](https://github.com/user-attachments/assets/0b18c9da-0f49-4652-bd2b-7d6202f428dc)

## 📌 디렉토리 구조

```
.
├─config
├─domain
│  ├─auth
│  │  ├─api
│  │  ├─controller
│  │  ├─dto
│  │  └─service
│  ├─donation
│  │  ├─api
│  │  ├─controller
│  │  ├─dto
│  │  ├─entity
│  │  ├─repository
│  │  └─service
│  ├─event
│  │  ├─api
│  │  ├─controller
│  │  ├─dto
│  │  ├─entity
│  │  ├─repository
│  │  └─service
│  ├─mail
│  │  ├─service
│  │  └─util
│  ├─participate
│  │  ├─api
│  │  ├─controller
│  │  ├─dto
│  │  ├─entity
│  │  ├─repository
│  │  └─service
│  ├─post
│  │  ├─api
│  │  ├─controller
│  │  ├─dto
│  │  ├─entity
│  │  ├─repository
│  │  └─service
│  ├─review
│  │  ├─api
│  │  ├─controller
│  │  ├─dto
│  │  ├─entity
│  │  ├─repository
│  │  └─service
│  ├─security
│  │  ├─common
│  │  ├─jwt
│  │  └─oauth
│  └─user
│      ├─api
│      ├─controller
│      ├─dto
│      ├─entity
│      ├─repository
│      └─service
├─exception
│  └─error
└─global
    ├─aop
    │  └─annotation
    ├─common
    │  ├─jwt
    │  └─response
    └─util
        ├─cookie
        ├─jwt
        └─redis
```

## 📌 컨벤션

### 브랜치 네이밍

```
main ── develop ── feature/티켓명
```

### 커밋 메시지

| emoji              | message | description                                           |
| ------------------ | ------- | ----------------------------------------------------- |
| :sparkles:         | feat    | 새로운 기능 추가, 기존 기능을 요구 사항에 맞추어 수정 |
| :bug:              | fix     | 기능에 대한 버그 수정                                 |
| :closed_book:      | docs    | 문서(주석) 수정                                       |
| :art:              | style   | 코드 스타일, 포맷팅에 대한 수정                       |
| :recycle:          | refact  | 기능 변화가 아닌 코드 리팩터링                        |
| :white_check_mark: | test    | 테스트 코드 추가/수정                                 |
| :pushpin:          | chore   | 패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore    |
