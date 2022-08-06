package com.jh.loginapi.member;

import com.jh.loginapi.config.JwtConfig;
import com.jh.loginapi.member.controller.MemberApiController;
import com.jh.loginapi.member.dto.entity.Members;
import com.jh.loginapi.security.WithMockJwtAuthentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MemberApiControllerTest {
    private MockMvc mockMvc;

    private JwtConfig jwtConfig;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Autowired
    public void setJwtConfig(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Test
    @DisplayName("로그인 성공 테스트 (아이디, 비밀번호가 올바른 경우)")
    void loginSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"tester@tester.com\",\"passwd\":\"1234\"}")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MemberApiController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.accessToken").exists())
                .andExpect(jsonPath("$.response.accessToken").isString())
                .andExpect(jsonPath("$.response.refreshToken").exists())
                .andExpect(jsonPath("$.response.refreshToken").isString());
    }

    @Test
    @DisplayName("로그인 실패 테스트 (아이디, 비밀번호가 올바르지 않은 경우)")
    void loginFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"tester@tester.com\",\"passwd\":\"4321\"}")
        );
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(MemberApiController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)));
    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("내 정보 조회 성공 테스트 (토큰이 올바른 경우)")
    void myProfileSuccessTest() throws Exception {
        Members members = new Members();
        members.setMemberNo(1L);
        ResultActions result = mockMvc.perform(
                get("/api/member/myProfile")
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MemberApiController.class))
                .andExpect(handler().methodName("myProfile"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.name", is("tester")))
                .andExpect(jsonPath("$.response.email", is("tester@tester.com")))
                .andExpect(jsonPath("$.response.nickname", is("Tester")))
                .andExpect(jsonPath("$.response.phoneNum", is("010-0000-0000")));
    }

    @Test
    @DisplayName("내 정보 조회 실패 테스트 (토큰이 올바르지 않을 경우)")
    void myProfileFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/member/myProfile")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(jwtConfig.getAccessHeader(), "Bearer " + randomAlphanumeric(60))
        );
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(401)))
                .andExpect(jsonPath("$.error.message", is("토큰이 필요합니다.")))
        ;
    }
}
