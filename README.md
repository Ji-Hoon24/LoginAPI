Login API
=
---

## 프로젝트 설명
해당 API는 로컬에서 로그인을 해볼 수 있도록 구성되어 있습니다.

로그인시 JWT로 생성된 `AccessToken`과 `RefreshToken`이 발급되며 `AccessToken`은 `5분`의 유효기간을 가지고, `RefreshToken`은 `10일`간의 유효기간을 가집니다.

발급된 토큰으로 통신을 진행할 수 있으며 HttpHeader에 `X-AUTH-TOKEN : Bearer 엑세스 토큰` 형태로 `AccessToken`을 넣어주어야 합니다.

`AccessToken`이 만료되었을 경우 `RefreshToken`을 `X-REFRESH-TOKEN : Bearer 리프레시 토큰` 형태로 `AccessToken`과 함께 보내주면 새로 발급된 토큰을 만들어줍니다. 

## 실행 방법


## 사용 기술

<p align="center">
<img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white">
<img src="https://img.shields.io/badge/JUnit4-25A162?style=for-the-badge&logo=JUnit4&logoColor=white">
<img src="https://img.shields.io/badge/H2-003545?style=for-the-badge&logo=H2&logoColor=white">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">
<img src="https://img.shields.io/badge/JWT-0099E5?style=for-the-badge&logo=JWT&logoColor=white">
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white">
</p>

> Java 1.8, SpringBoot 2.7.2를 기반으로 프로젝트를 구성하였습니다.<br>
> 데이터베이스는 따로 설치하거나 셋팅할 필요 없이<br>
> 인메모리 DB인 H2 Database와 Embedded-Redis로 어디서든 동일하게 테스트 할 수 있게 작성되었습니다.<br> 

## 구현 스펙
### 공통 참고사항
모든 API는 application/json의 형태로 요청 및 응답을 진행합니다.

API의 응답 형태는 아래의 예시 형태를 사용합니다.

### <Object 형태>
```json
{
    "success": true,
    "response": {
        "Data": "example"
    },
    "error": null
}
```

### <Boolean 형태> 
```json
{
    "success": true,
    "response": true,
    "error": null
}
```

### <Error 케이스>
```json
{
    "success": false,
    "response": null,
    "error": {
      "status" : "400",
      "message" : "닉네임은 필수 입력 값입니다."
    }
}
```

### 1. 회원
#### 1-1. 회원가입
POST : /api/member/join

* 필수 조건
  + [전화번호 인증 필수!](#2-1-전화번호-인증번호-발송)
  + 이메일, 닉네임, 전화번호가 중복되지 않을 것!

### <데이터 예시>
```json
{
    "email":"tt@tester.com",
    "name":"테스터",
    "passwd":"1q2w3e4r!@",
    "nickname":"테스터입니다",
    "phoneNum":"010-1234-5678"
}
```

#### 1-2. 로그인
POST : /api/member/login

* 필수 조건
  + 아이디(이메일), 비밀번호 필수!

### <데이터 예시>
```json
{
    "email":"tester@tester.com",
    "passwd":"1234"
}
```

#### 1-3. 비밀번호 재설정
PUT : /api/member/passwdReset

* 필수 조건
  + [전화번호 인증 필수!](#2-1-전화번호-인증번호-발송)
  + 아이디(이메일), 비밀번호, 전화번호 필수!

### <데이터 예시>
```json
{
    "email" : "tester@tester.com",
    "phoneNum" : "010-0000-0000",
    "newPasswd" : "1q2w3e4r!@"
}
```

#### 1-4. 프로필 조회
GET : /api/member/myProfile

* 필수 조건
  + AccessToken 필수!

### <데이터 예시>
```
HttpHeader에 AccessToken 아래의 형태로 추가

X-AUTH-TOKEN : Bearer AccessToken 
```

### 2. 인증
#### 2-1. 전화번호 인증번호 발송
POST : /api/auth/sendAuth

해당 API는 회원가입, 비밀번호 재설정시 반드시 선행되어야 하는 API입니다.
실제 SMS발송으로 연동하지 않아서 임시로 응답에 인증번호를 보내도록 되어있습니다.
해당 인증번호를 [전화번호 인증번호 검증](#2-2-전화번호-인증번호-검증)에 입력해주어야 합니다.

* 필수 조건
  + 전화번호 필수!

### <데이터 예시>
```json
{
    "phoneNum": "010-0000-0000"
}
```

#### 2-2. 전화번호 인증번호 검증
POST : /api/auth/validAuth

* 필수 조건
  + 전화번호, 인증번호 필수!

### <데이터 예시>

```json
{
  "phoneNum": "010-0000-0000",
  "authCode": "1234"
}
```

#### 2-3. 리프레시 토큰으로 엑세스 토큰 발급
POST : /api/auth/refresh

* 필수 조건
  + AccessToken 필수!
  + RefreshToken 필수!

### <데이터 예시>
```
HttpHeader에 AccessToken, RefreshToken을 아래의 형태로 추가

X-AUTH-TOKEN : Bearer AccessToken
X-REFRESH-TOKEN : Bearer RefreshToken  
```

## 신경쓴 부분
