package com.desiredchargingstation.map.config;

import com.desiredchargingstation.map.security.CustomUserDetailsService;
import com.desiredchargingstation.map.security.oauth2.CustomOAuth2UserService;
import com.desiredchargingstation.map.security.oauth2.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile({"dev", "test"})
public class SecurityConfigDev extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()

                .headers()
                .disable()

                .httpBasic()
                .disable()

                .cors()
                .and()

                .authorizeRequests()
                .antMatchers("/", "/favicon.ico", "/login", "/all-points")
                .permitAll()

                .anyRequest()
                .authenticated()

                .and()

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                                .oidcUserService(customOidcUserService))
                        .defaultSuccessUrl("http://localhost:3000", true)
                        .failureUrl("/")
                )

                .logout(logout -> logout
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/")
                );

    }
}
