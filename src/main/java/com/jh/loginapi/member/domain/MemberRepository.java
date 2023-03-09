package com.jh.loginapi.member.domain;


import com.jh.loginapi.member.dto.entity.Members;
import com.jh.loginapi.member.dto.request.JoinRequest;
import com.jh.loginapi.member.dto.request.PasswdResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Members, Long> {

    Optional<Members> findByEmail(String email);

    Optional<Members> findByNickname(String nickname);

    Optional<Members> findByPhoneNum(String phoneNum);

    Optional<Members> findByMemberNo(long memberNo);

}
