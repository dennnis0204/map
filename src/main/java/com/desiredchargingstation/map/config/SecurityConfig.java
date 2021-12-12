package com.desiredchargingstation.map.config;

import com.desiredchargingstation.map.security.CustomUserDetailsService;
import com.desiredchargingstation.map.security.RestAuthenticationEntryPoint;
import com.desiredchargingstation.map.security.oauth2.CustomOAuth2UserService;
import com.desiredchargingstation.map.security.oauth2.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile({"prod"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${settings.host}")
    private String host;

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
                .csrf(csrf -> csrf
                        .csrfTokenRepository((CookieCsrfTokenRepository.withHttpOnlyFalse())))

                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()

                .authorizeRequests()
                .antMatchers("/", "/favicon.ico", "/all-points", "/js/**", "/css/**")
                .permitAll()

                .anyRequest()
                .authenticated()

                .and()

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                                .oidcUserService(customOidcUserService))
                        .defaultSuccessUrl(host, true)
                        .failureUrl("/")
                )

                .logout(logout -> logout
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl(host)
                );

    }
}
