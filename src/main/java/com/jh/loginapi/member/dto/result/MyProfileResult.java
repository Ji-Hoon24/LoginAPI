package com.jh.loginapi.member.dto.result;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MyProfileResult {

    private String name;

    private String email;

    private String nickname;

    private String phoneNum;

}
