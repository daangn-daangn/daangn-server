package com.daangndaangn.apiserver.controller;

import com.daangndaangn.apiserver.HealthCheckController;
import com.daangndaangn.apiserver.security.WithMockJwtAuthentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockJwtAuthentication
    @DisplayName("회원 인증이 필요한 API는 @WithMockJwtAuthentication을 사용해서 호출할 수 있다.")
    void getSystemTimeMillisWithUserSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/health/with-user")
                    .accept(MediaType.APPLICATION_JSON)
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(HealthCheckController.class))
                .andExpect(handler().methodName("getSystemTimeMillisWithUser"))
                .andExpect(jsonPath("$.success", is(true)));
    }
}