package me.benny.practice.spring.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest // SpringBootTest
@Transactional // Rollback을 위한 Transactional
@ActiveProfiles("test") // 테스트 수행시 특정 빈만 로드하면서 테스트를 수행할 수 있음
class NoticeControllerTest {
    private MockMvc mockMvc; // 가짜 유저를 만드는 것
    @BeforeEach
    public void setUp(@Autowired WebApplicationContext webApplicationContext){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity()) // MockMvc에 springSecurity를 적용하는 것이다. ( 안그러면 springSecurity가 적용이 안된다. )
                .alwaysDo(print()) // 결과 찍어주기
                .build();
    }
    @Test
    void getNotice() {
    }

    @Test
    void postNotice() {
    }

    @Test
    void deleteNotice() {
    }
}