package com.jh.loginapi.member.domain;


import com.jh.loginapi.member.dto.entity.Members;
import com.jh.loginapi.member.dto.request.JoinRequest;
import com.jh.loginapi.member.dto.request.PasswdResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface MemberRepository extends JpaRepository<Members, Long> {

    Optional<Members> findByEmail(String email);

    Optional<Members> findByNickname(String nickname);

    Optional<Members> findByPhoneNum(String phoneNum);

    List<Members> findByEmailOrNicknameOrPhoneNum(String email, String nickname, String phoneNum);
    Optional<Members> findByMemberNo(long memberNo);

}
