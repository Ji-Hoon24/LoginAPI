package com.jh.loginapi.member.controller;

import com.jh.loginapi.config.ApiResultUtil.ApiResult;
import com.jh.loginapi.member.dto.request.JoinRequest;
import com.jh.loginapi.member.dto.request.LoginRequest;
import com.jh.loginapi.member.dto.result.LoginResult;
import com.jh.loginapi.member.dto.result.MyProfileResult;
import com.jh.loginapi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.jh.loginapi.config.ApiResultUtil.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberApiController {

    private final MemberService memberService;

    //TODO 1. 회원가입
    @PostMapping("/join")
    public ApiResult<Boolean> join(@Valid @RequestBody JoinRequest joinRequest) {
        boolean result = memberService.join(joinRequest);
        return success(result);
    }

    //TODO 2. 로그인
    @PostMapping("/login")
    public ApiResult<LoginResult> login(@RequestBody LoginRequest loginRequest) {
        LoginResult result = memberService.login(loginRequest);
        return success(result);
    }

    //TODO 3. 비밀번호 찾기 (재설정)
    @PostMapping("/passwdReset")
    public ApiResult<?> passwdReset() {
        return success(true);
    }

    @GetMapping("/myProfile")
    public ApiResult<MyProfileResult> myProfile(@AuthenticationPrincipal String SMemberNo) {
        long memberNo = Long.parseLong(SMemberNo);
        MyProfileResult result = memberService.myProfile(memberNo);

        return success(result);
    }
}
