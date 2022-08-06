DROP TABLE IF EXISTS members CASCADE;

CREATE TABLE members (
        member_no       bigint NOT NULL AUTO_INCREMENT, --사용자 PK
        name            varchar(10) NOT NULL,           --사용자명
        email           varchar(50) NOT NULL,           --로그인 이메일
        passwd          varchar(80) NOT NULL,           --로그인 비밀번호
        nickname        varchar(50) NOT NULL,           --사용자 닉네임
        phone_num       varchar(20) NOT NULL,           --사용자 전화번호
        login_count     int NOT NULL DEFAULT 0,         --로그인 횟수. 로그인시 1씩 증가
        last_login_dt   datetime DEFAULT NULL,          --최종 로그인 일자
        create_dt       datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(),
        PRIMARY KEY (member_no),
        CONSTRAINT unq_user_email UNIQUE (email),
        CONSTRAINT unq_user_nickname UNIQUE (nickname),
        CONSTRAINT unq_user_phone_num UNIQUE (phone_num)
);