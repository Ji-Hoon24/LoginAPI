package com.jh.loginapi.member.dto.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;


@Entity
@Table(name="members")
@Getter
public class Members {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "members_seq")
    @Schema(description = "사용자 고유값(PK)")
    private long memberNo;

    @Schema(description = "사용자명", example = "테스터")
    private String name;

    @Schema(description = "로그인 이메일")
    private String email;

    @Schema(description = "로그인 비밀번호")
    private String passwd;

    @Schema(description = "사용자 닉네임")
    private String nickname;

    @Schema(description = "사용자 전화번호")
    private String phoneNum;

    @Schema(description = "로그인 횟수. 로그인시 1씩 증")
    private int loginCount;

    @Schema(description = "최종 로그인 일자")
    private LocalDateTime lastLoginDt;

    @Schema(hidden = true)
    private LocalDateTime createDt;

    public void afterLoginSuccess() {
        this.loginCount++;
        this.lastLoginDt = now();
    }
}