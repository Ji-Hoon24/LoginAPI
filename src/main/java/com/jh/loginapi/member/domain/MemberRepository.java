package com.jh.loginapi.member.domain;


import com.jh.loginapi.member.dto.entity.Members;
import com.jh.loginapi.member.dto.request.JoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Members, Long> {

    Optional<Members> findByEmail(String email);

    Optional<Members> findByNickname(String nickname);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Members(name,email,passwd,nickname,phone_num) VALUES (:#{#joinRequest.name}, :#{#joinRequest.email}, :#{#joinRequest.encodePasswd}, :#{#joinRequest.nickname}, :#{#joinRequest.phoneNum})", nativeQuery = true)
    Optional<Integer> save(JoinRequest joinRequest);

    Optional<Members> findByMemberNo(long memberNo);

}
