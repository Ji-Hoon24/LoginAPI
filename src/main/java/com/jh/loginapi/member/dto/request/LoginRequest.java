package com.jh.loginapi.member.dto.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;

    private String passwd;

}
