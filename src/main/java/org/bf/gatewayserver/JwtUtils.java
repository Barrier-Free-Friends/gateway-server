package org.bf.gatewayserver;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import javax.crypto.SecretKey;

@Slf4j
@Component
public class JwtUtils {
    public static final String AUTHORIZATION_HEADER = HttpHeaders.AUTHORIZATION;
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";

    private final SecretKey key;

    public JwtUtils(@Value("${jwt.secret}") String key) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    public static String extractTokenFromHeader(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String bearerToken = request.getHeaders().getFirst(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_TOKEN_PREFIX)) {
            return bearerToken.substring(BEARER_TOKEN_PREFIX.length());
        }

        return null;
    }

    public boolean validateToken(String token) throws Exception {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            // 잘못 구성된 JWT 또는 Signature 오류 (토큰 위변조)
            throw new MalformedJwtException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            // 토큰 만료
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            // 지원되지 않는 JWT 형식
            throw new UnsupportedJwtException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            // 토큰 문자열이 비어있거나 null인 경우 (일반적으로 추출 단계에서 걸러짐)
            throw new IllegalArgumentException("JWT 토큰이 잘못되었습니다.");
        }
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
