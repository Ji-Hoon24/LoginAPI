package com.jh.loginapi.member.dto.result;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResult {

    private String accessToken;

    private String refreshToken;

}
