package com.example.config;

import com.alibaba.fastjson2.JSONObject;
import com.example.entity.RestBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize->authorize
                        .anyRequest()
                        .authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/auth/login")
                        .permitAll()
                        .successHandler(this::onAuthenticationSuccess)
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable);
                return http.build();
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        request.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功")));
    }
}
