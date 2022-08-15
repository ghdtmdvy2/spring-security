package me.benny.practice.spring.security.config;

import lombok.RequiredArgsConstructor;
import me.benny.practice.spring.security.user.User;
import me.benny.practice.spring.security.user.UserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security 설정 Config
 */
@Configuration //
@EnableWebSecurity // WebSecurityConfigurerAdapter를 상속을 할 경우 달아줘야한다.
// WebSecurityConfigurerAdapter : 개발자가 Spring Security를 설정을 쉽게 할 수 있도록 구현 되어 있다.
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // basic authentication filter
        http.httpBasic().disable(); // basic filter 비 활성화
        // csrf 사용
        http.csrf();
        // rememberMe(로그인 유지) 사용
        http.rememberMe();

        // authorization
        http.authorizeRequests()
                // /와 /home와 /signup 는 접근 권한 없이 모두 허용
                .antMatchers("/","/home","/signup").permitAll()
                // 권한이 USER인 경우에만 접근 가능
                .antMatchers("/note").hasRole("USER")
                // 권한이 ADMIN인 경우에만 접근 가능
                .antMatchers("/admin").hasRole("ADMIN")
                // GET 요청으로 "/notice" URL을 접속 했을 때 인증 받은 사람만 접근이 가능하다. <- 공지사항 조회 기능
                .antMatchers(HttpMethod.GET,"/notice").authenticated()
                // POST 요청으로(공지사항 작성) "/notice" URL을 접속 했을 때 ADMIN 권한만 가능하다.
                .antMatchers(HttpMethod.POST,"/notice").hasRole("ADMIN")
                // DELETE 요청으로(공지사항 삭제) "/notice" URL을 접속 했을 때 ADMIN 권한만 가능하다.
                .antMatchers(HttpMethod.DELETE,"/notice").hasRole("ADMIN")
                // 그 외 요청은 인증 받은 사람만 가능하게 만듦. ( 그러므로 antMatchers(HttpMethod.GET,"/notice").authenticated() 코드는 없어도 된다. )
                .anyRequest().authenticated();
        // login
        http.formLogin()
                // login 페이지가 어떤 것인지 설정.
                .loginPage("/login")
                // login 성공시 루트 페이지로 이동 ( alwayUse를 false로 입력 시 접속 하려던 URL로 바로 이동 )
                .defaultSuccessUrl("/",false)
                // login은 모두 접근 가능
                .permitAll();
        // logout
        http.logout()
                // logout 요청 경로
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // logout 성공 시 루트 페이지로 이동
                .logoutSuccessUrl("/");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // web.ignoring().antMatchers("/images/**", "/css/**"); // 아래 코드와 같은 코드입니다.
        // 정적 리소스 spring security 대상에서 제외
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());

        // 위의 코드와 똑같이 작동은 하지만 밑에 코드는 필터를 거치나 허가를 해주는 것이고, 위에 코드는 필터를 거치지 않기 때문에
        // 위에 코드가 더 성능적으로 우수하다.
        // http.authorizeRequests()
        //     .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        //     .permitAll();
    }
}
