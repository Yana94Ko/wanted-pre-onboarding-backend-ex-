package wanted.preonboarding.backend.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import wanted.preonboarding.backend.global.security.jwt.CustomAccessDeniedHandler;
import wanted.preonboarding.backend.global.security.jwt.JwtAuthenticationProcessingFilter;
import wanted.preonboarding.backend.global.security.jwt.JwtAuthorizationFilter;
import wanted.preonboarding.backend.global.security.jwt.JwtProperties;
import wanted.preonboarding.backend.global.security.jwt.JwtUtils;
import wanted.preonboarding.backend.member.service.MemberService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MemberService memberService;
    private final JwtUtils jwtUtils;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests) -> {authorizeRequests
                        .requestMatchers("/").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/member/signup").permitAll()
                        .anyRequest().authenticated();
                })
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) -> {sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilterBefore(
                        new JwtAuthenticationProcessingFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))
                                , jwtUtils),
                        UsernamePasswordAuthenticationFilter.class
                ).addFilterBefore(
                        new JwtAuthorizationFilter(memberService, jwtUtils),
                        BasicAuthenticationFilter.class
                )
                .exceptionHandling((exceptionHandling)->exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // get 방식으로도 동작
                        .logoutSuccessUrl("/")
                        .deleteCookies(JwtProperties.COOKIE_NAME)
                        .invalidateHttpSession(true)
                )
                .getOrBuild();
    }
}