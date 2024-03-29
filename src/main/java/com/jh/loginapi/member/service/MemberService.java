package com.jh.loginapi.member.service;

import com.jh.loginapi.config.JwtConfig;
import com.jh.loginapi.member.dto.entity.Members;
import com.jh.loginapi.member.dto.request.JoinRequest;
import com.jh.loginapi.member.dto.request.LoginRequest;
import com.jh.loginapi.member.dto.request.PasswdResetRequest;
import com.jh.loginapi.member.dto.result.LoginResult;
import com.jh.loginapi.member.domain.MemberRepository;
import com.jh.loginapi.member.dto.result.MyProfileResult;
import com.jh.loginapi.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final JwtConfig jwtConfig;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final RedisService redisService;

    @Transactional(rollbackFor = Exception.class)
    public void join(JoinRequest joinRequest) {
        this.validJoin(joinRequest);
        String encodePasswd = passwordEncoder.encode(joinRequest.getPasswd());
        joinRequest.setEncodePasswd(encodePasswd);

        Members members = Members.builder()
                .email(joinRequest.getEmail())
                .name(joinRequest.getName())
                .nickname(joinRequest.getNickname())
                .phoneNum(joinRequest.getPhoneNum())
                .passwd(encodePasswd)
                .build();


        memberRepository.save(members);
        redisService.delete(joinRequest.getPhoneNum());
    }

    private void validJoin(JoinRequest joinRequest) {
        List<Members> members = memberRepository.findByEmailOrNicknameOrPhoneNum(joinRequest.getEmail(), joinRequest.getNickname(), joinRequest.getPhoneNum());
        for(Members membersEntity : members) {
            if(membersEntity.getEmail().equals(joinRequest.getEmail())) {
                throw new IllegalArgumentException("중복된 이메일이 있습니다.");
            }

            if(membersEntity.getNickname().equals(joinRequest.getNickname())) {
                throw new IllegalArgumentException("중복된 닉네임이 있습니다.");
            }

            if(membersEntity.getPhoneNum().equals(joinRequest.getPhoneNum())) {
                throw new IllegalArgumentException("중복된 전화번호가 있습니다.");
            }
        }

        String checkPhoneAuth = redisService.findPhoneAuthSuccess(joinRequest.getPhoneNum());
        if(checkPhoneAuth == null || !checkPhoneAuth.equals("Y")) {
            throw new IllegalArgumentException("전화번호 인증이 필요합니다.");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public LoginResult login(LoginRequest loginRequest) {
        Members members = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("아이디와 패스워드를 다시 확인 바랍니다."));

        if (!passwordEncoder.matches(loginRequest.getPasswd(), members.getPasswd())) {
            throw new IllegalArgumentException("아이디와 패스워드를 다시 확인 바랍니다.");
        }

        members.afterLoginSuccess();
        memberRepository.save(members);

        return redisService.tokenCreate(members);
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

    @Transactional(rollbackFor = Exception.class)
    public void passwdReset(PasswdResetRequest request) {
        Members membersEntity = this.validPasswdReset(request);
        String encodePasswd = passwordEncoder.encode(request.getNewPasswd());
        membersEntity.updatePassword(encodePasswd);
        memberRepository.save(membersEntity);
        redisService.delete(request.getPhoneNum());
    }

    private Members validPasswdReset(PasswdResetRequest request) {
        Members membersEntity = memberRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("이메일 정보가 없습니다."));

        String checkPhoneAuth = redisService.findPhoneAuthSuccess(request.getPhoneNum());
        if(checkPhoneAuth == null || !checkPhoneAuth.equals("Y")) {
            throw new IllegalArgumentException("전화번호 인증이 필요합니다.");
        }

        return membersEntity;
    }
}
