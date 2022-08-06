package com.jh.loginapi.auth.dto.result;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthResult {

    private String authCode;

}
