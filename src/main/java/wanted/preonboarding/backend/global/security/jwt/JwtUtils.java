package wanted.preonboarding.backend.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final JwtKey jwtKey;
    /**
     * 토큰에서 username(email) 찾기
     *
     * @param token
     * @return username
     */

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKeyResolver(new SigningKeyResolver(jwtKey))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public String createToken(UserDetails user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        Date now= new Date();
        Map.Entry<String, Key> key = jwtKey.getRandomKey();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + JwtProperties.EXPIRATION_TIME))
                .setHeaderParam(JwsHeader.KEY_ID, key.getKey())
                .signWith(key.getValue())
                .compact();
    }
}
