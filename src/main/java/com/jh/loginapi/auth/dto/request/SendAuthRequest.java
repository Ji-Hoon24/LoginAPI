package com.jh.loginapi.auth.dto.request;

import lombok.Data;

@Data
public class SendAuthRequest {

    private String phoneNum;

    private String authCode;

}
