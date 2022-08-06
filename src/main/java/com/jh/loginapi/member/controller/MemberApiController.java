package com.jh.loginapi.member.controller;

import com.jh.loginapi.config.ApiResultUtil.ApiResult;
import com.jh.loginapi.member.dto.request.JoinRequest;
import com.jh.loginapi.member.dto.request.LoginRequest;
import com.jh.loginapi.member.dto.request.PasswdResetRequest;
import com.jh.loginapi.member.dto.result.LoginResult;
import com.jh.loginapi.member.dto.result.MyProfileResult;
import com.jh.loginapi.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.jh.loginapi.config.ApiResultUtil.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberApiController {

    private final MemberService memberService;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    @ApiOperation(value = "회원가입(전화번호 인증 필수)")
    @PostMapping("/join")
    public ApiResult<Boolean> join(@Valid @RequestBody JoinRequest joinRequest) {
        boolean result = memberService.join(joinRequest);
        return success(result);
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ApiResult<LoginResult> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResult result = memberService.login(loginRequest);

        return success(result);
    }


    @ApiOperation(value = "비밀번호 재설정(전화번호 인증 필수)")
    @PostMapping("/passwdReset")
    public ApiResult<Boolean> passwdReset(@Valid @RequestBody PasswdResetRequest request) {
        boolean result = memberService.passwdReset(request);
        return success(result);
    }

    @ApiOperation(value = "내 프로필 조회(엑세스 토큰 필수)")
    @GetMapping("/myProfile")
    public ApiResult<MyProfileResult> myProfile(@ApiParam(hidden = true) @AuthenticationPrincipal String SMemberNo) {
        long memberNo = Long.parseLong(SMemberNo);
        MyProfileResult result = memberService.myProfile(memberNo);

        return success(result);
    }
}
