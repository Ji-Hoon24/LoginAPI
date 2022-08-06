package com.jh.loginapi.auth.controller;

import com.jh.loginapi.auth.dto.request.SendAuthRequest;
import com.jh.loginapi.auth.dto.result.AuthResult;
import com.jh.loginapi.auth.service.AuthService;
import com.jh.loginapi.config.ApiResultUtil.ApiResult;
import com.jh.loginapi.config.JwtConfig;
import com.jh.loginapi.member.dto.result.LoginResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.jh.loginapi.config.ApiResultUtil.*;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthApiController {

    private final AuthService authService;

    //TODO 1. 핸드폰 인증 발송
    @PostMapping("/sendAuth")
    public ApiResult<AuthResult> sendAuth(@RequestBody SendAuthRequest sendAuthRequest) {
        AuthResult result = authService.sendAuth(sendAuthRequest);
        return success(result);
    }

    //TODO 2. 핸드폰 인증 확인
    @PostMapping("/validAuth")
    public ApiResult<?> validAuth(@RequestBody SendAuthRequest sendAuthRequest) {
        boolean result = authService.validAuth(sendAuthRequest);
        return success(result);
    }

    @PostMapping("/refresh")
    public ApiResult<LoginResult> refresh(HttpServletRequest request) {
        LoginResult result = authService.newAccessToken(request);
        return success(result);
    }
}
