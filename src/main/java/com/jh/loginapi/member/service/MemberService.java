package com.jh.loginapi.member.service;

import com.jh.loginapi.config.JwtConfig;
import com.jh.loginapi.member.dto.entity.Members;
import com.jh.loginapi.member.dto.entity.Role;
import com.jh.loginapi.member.dto.request.JoinRequest;
import com.jh.loginapi.member.dto.request.LoginRequest;
import com.jh.loginapi.member.dto.result.LoginResult;
import com.jh.loginapi.member.domain.MemberRepository;
import com.jh.loginapi.member.dto.result.MyProfileResult;
import com.jh.loginapi.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final JwtConfig jwtConfig;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;


    private final RedisService redisService;


    public boolean join(JoinRequest joinRequest) {
        this.validJoin(joinRequest);
        String encodePasswd = passwordEncoder.encode(joinRequest.getPasswd());
        joinRequest.setEncodePasswd(encodePasswd);

        Optional<Integer> result = memberRepository.save(joinRequest);
        if(!result.isPresent() && result.get() == 0) return false;

        return true;
    }

    private void validJoin(JoinRequest joinRequest) {
        Optional<Members> emailCheck = memberRepository.findByEmail(joinRequest.getEmail());
        if(emailCheck.isPresent()) {
            throw new IllegalArgumentException("중복된 이메일이 있습니다.");
        }
        Optional<Members> nicknameCheck = memberRepository.findByNickname(joinRequest.getNickname());
        if(nicknameCheck.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임이 있습니다.");
        }
    }

    public LoginResult login(LoginRequest loginRequest) {
        /**
         * 1. 메일을 기준으로 유저 정보 확인
         * 2. 비밀번호 매칭으로 여부 확인
         * 3. 최근 로그인 시간 및 로그인 카운트 증가
         * 4. JWT 토큰 생성 및 Redis 저장
         * */
        Members members = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("아이디와 패스워드를 다시 확인 바랍니다."));

        if(!passwordEncoder.matches(loginRequest.getPasswd(), members.getPasswd())){
            throw new IllegalArgumentException("아이디와 패스워드를 다시 확인 바랍니다.");
        }

        members.afterLoginSuccess();
        memberRepository.save(members);

        return this.tokenCreate(members);
    }

    public LoginResult tokenCreate(Members members) {
        String accessToken = jwtConfig.createAccessToken(members);
        String refreshToken = jwtConfig.createRefreshToken();

        redisService.saveRefreshToken(members.getMemberNo(), refreshToken);
        return LoginResult.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public MyProfileResult myProfile(long memberNo) {
        Members members = memberRepository.findByMemberNo(memberNo).orElseThrow(
                () -> new IllegalArgumentException("회원정보가 없습니다.")
        );

        return MyProfileResult.builder()
                .email(members.getEmail())
                .name(members.getName())
                .nickname(members.getNickname())
                .phoneNum(members.getPhoneNum())
                .build();
    }
}
