package me.benny.practice.spring.security.note;

import me.benny.practice.spring.security.user.User;
import me.benny.practice.spring.security.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // SpringBootTest
@Transactional // Rollback을 위한 Transactional
@ActiveProfiles("test") // 테스트 수행시 특정 빈만 로드하면서 테스트를 수행할 수 있음
class NoteControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @BeforeEach
    public void setUp(@Autowired WebApplicationContext webApplicationContext){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity()) // MockMvc에 springSecurity를 적용하는 것이다. ( 안그러면 springSecurity가 적용이 안된다. )
                .alwaysDo(print()) // 결과 찍어주기
                .build();
        userRepository.save(new User("user123","user123","ROLE_USER")); // TEST로 USER를 만듦.
    }
    @Test
    void getNote_인증없음() throws Exception {
        mockMvc.perform(get("/notice")) // notice URL를 get으로 요청
                .andExpect(redirectedUrlPattern("**/login")) // login을 하지 않았기 때문에 login 창으로 redirect가 된다.
                .andExpect(status().is3xxRedirection()); // 이것은 redirection을 했다는 뜻이다.
    }

    @Test
    // @WithMockUser와 다르게 가짜 유저를 가져올 때 UserDetails를 통해 가져오게 된다.
    @WithUserDetails(
            value = "user123", // 가져올 username
            userDetailsServiceBeanName = "userDetailsService", // userDetailsService 를 Bean으로 가져오겠다. ( 즉 이 메서드로 찾아 오겠다. )
            setupBefore = TestExecutionEvent.TEST_EXECUTION // Test 실행 직전에 MockUser를 만들겠다는 뜻 ( BeforeEach에서 test 유저 만든 것을 실행 직전에 만듦 )
    )
    void getNote_유저권한있음() throws Exception {
        mockMvc.perform(get("/notice")) // notice URL를 get으로 요청
                .andExpect(status().isOk()); // 이것은 redirection을 했다는 뜻이다.
    }

    @Test
    void saveNote() {
    }

    @Test
    void deleteNote() {
    }
}