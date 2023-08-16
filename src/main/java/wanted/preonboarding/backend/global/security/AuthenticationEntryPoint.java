package wanted.preonboarding.backend.global.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component("CustomAuthenticationEntryPoint")
public class AuthenticationEntryPoint implements
        org.springframework.security.web.AuthenticationEntryPoint {
    //DefaultHandelrExceptionResolver 주입
    private final HandlerExceptionResolver resolver;

    public AuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver){
        this.resolver = resolver;
    }
    //예외 처리기를 해석기에 위임함으로써, Security의 예외를 예외처리기 메서드를 통해 컨트롤러로 처리 가능
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        resolver.resolveException(request, response, null, authException);
    }
}
