package wanted.preonboarding.backend.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import wanted.preonboarding.backend.member.entity.Member;
import wanted.preonboarding.backend.member.service.MemberService;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final MemberService memberService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;
        if(request.getCookies() != null){
            try {
                token = Arrays.stream(request.getCookies())
                        .filter(cookie -> cookie.getName().equals(JwtProperties.COOKIE_NAME)).findFirst()
                        .map(Cookie::getValue)
                        .orElse(null);
            } catch (Exception e) {
                log.error("쿠키가 비어있습니다.");
                e.printStackTrace();
                // TODO : 별도로 예외처리/로그 필요한 경우 추가
            }
        }


        if (token != null) {
            try {
                // SecurityContext에 Authentication 값 설정해주기
                UsernamePasswordAuthenticationToken authentication = getUsernamePasswordAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                e.printStackTrace();
                Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, null);
                cookie.setMaxAge(0); // return 시 삭제됨
                response.addCookie(cookie);
            }
        }

        filterChain.doFilter(request, response);
    }
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthentication(String token){
        String username = jwtUtils.getUsername(token);

        if(username != null){
            Member member = memberService.getMemberByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("아이디 \""+username + "\"는 없는 회원입니다"));

            List<GrantedAuthority> authorities = member.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.name()))
                    .collect(Collectors.toList());

            return new UsernamePasswordAuthenticationToken (
                    member,
                    null,
                    authorities
            );
        }
        return null;
    }


}
