package com.moppletop.dsb.security;

import com.moppletop.dsb.system.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@RequiredArgsConstructor
public class DSBSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/static/favicon.ico", "/css/**", "/js/**", "/images/**", "/scss/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .defaultSuccessUrl("/")
                    .loginProcessingUrl("/authorise")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll();

//        http
//                .csrf()
//                .disable()
//                .headers()
//                .frameOptions()
//                .disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new DSBUserDetailsService(userService));
    }
}
