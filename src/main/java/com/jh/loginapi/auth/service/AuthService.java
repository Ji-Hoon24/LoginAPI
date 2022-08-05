package com.jh.loginapi.auth.service;

import com.jh.loginapi.config.JwtConfig;
import com.jh.loginapi.exception.UnauthorizedException;
import com.jh.loginapi.member.dto.entity.Members;
import com.jh.loginapi.member.dto.result.LoginResult;
import com.jh.loginapi.member.service.MemberService;
import com.jh.loginapi.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtConfig jwtConfig;

    private final RedisService redisService;

    private final MemberService memberService;

    public LoginResult newAccessToken(HttpServletRequest request) {
        String accessToken = jwtConfig.extractAccessToken(request).orElseThrow(
                () -> new UnauthorizedException("엑세스 토큰이 필요합니다.")
        );
        String refreshToken = jwtConfig.extractRefreshToken(request).orElseThrow(
                () -> new UnauthorizedException("리프레시 토큰이 없습니다. 로그인이 필요합니다.")
        );

        Optional<String> SMemberNo = jwtConfig.extractMemberNo(accessToken);
        long memberNo = 0;
        if(SMemberNo.isPresent()) {
            memberNo = Long.parseLong(SMemberNo.get());
        }

        String redisRefreshToken = redisService.findRefreshToken(memberNo);

        if(refreshToken.equals(redisRefreshToken) && jwtConfig.isTokenValid(refreshToken)) {
            Members members = new Members();
            members.setMemberNo(memberNo);
            LoginResult result = memberService.tokenCreate(members);
            return result;
        }
        throw new UnauthorizedException("로그인이 필요합니다.");
    }

}
