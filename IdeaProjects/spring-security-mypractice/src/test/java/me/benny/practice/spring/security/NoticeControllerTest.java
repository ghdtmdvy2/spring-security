package me.benny.practice.spring.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // SpringBootTest
@Transactional // Rollback을 위한 Transactional
@ActiveProfiles("test") // 테스트 수행시 특정 빈만 로드하면서 테스트를 수행할 수 있음
class NoticeControllerTest {
    private MockMvc mockMvc;
    @BeforeEach
    public void setUp(@Autowired WebApplicationContext webApplicationContext){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity()) // MockMvc에 springSecurity를 적용하는 것이다. ( 안그러면 springSecurity가 적용이 안된다. )
                .alwaysDo(print()) // 결과 찍어주기
                .build();
    }

    @Test
    void getNotice_인증없음() throws Exception {
        mockMvc.perform(get("/notice")) // notice URL를 get으로 요청
                .andExpect(redirectedUrlPattern("**/login")) // login을 하지 않았기 때문에 login 창으로 redirect가 된다.
                .andExpect(status().is3xxRedirection()); // 그리고 상태코드가 3xx 에러가 뜰 것이다.
    }
    @Test
    @WithMockUser // 가짜 유저를 만들기 위한 어노테이션 ( 기본값은 name : user, password : password, roles : USER 이다. )
    void getNotice_인증있음() throws Exception {
        mockMvc.perform(get("/notice")) // notice URL를 get으로 요청
                .andExpect(status().isOk()); // 상태코드가 뜨지 않았을 때 == 정상적으로 작동 되었을 때
    }

    @Test
    void postNotice() {
    }

    @Test
    void deleteNotice() {
    }
}