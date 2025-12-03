package org.bf.gatewayserver.config;

import io.jsonwebtoken.*;
import org.bf.gatewayserver.JwtUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class AddUserInfoFilter implements GlobalFilter, Ordered {

    private final JwtUtils jwtUtils;

    private static final String HEADER_USER_ID = "X-User-Id";   // 사용자 식별 ID
    private static final String HEADER_USERNAME = "X-Username"; // 로그인 아이디
    private static final String HEADER_EMAIL = "X-User-Email";  // 이메일
    private static final String HEADER_NICKNAME = "X-Nickname"; // 닉네임
    private static final String HEADER_ROLES = "X-User-Roles"; // 권한

    public AddUserInfoFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println(exchange.getRequest().getHeaders());
        String token = JwtUtils.extractTokenFromHeader(exchange);

        if (token == null) {
            // 토큰이 없으면 그냥 통과
            return chain.filter(exchange);
        }

        return Mono.defer(() -> {
            try {
                jwtUtils.validateToken(token);
            } catch (Exception e) {
                return handleUnauthenticated(exchange, e.getMessage());
            }

            Claims claims = jwtUtils.getAllClaimsFromToken(token);

            String userId = claims.get("userId", String.class);
            String username = claims.get("username", String.class);
            String email = claims.get("email", String.class);

            List<String> rolesList = claims.get("roles", List.class);

            String rolesString = rolesList != null && !rolesList.isEmpty()
                    ? String.join(",", rolesList)
                    // roles가 비어있다면 기본값 "ROLE_USER" 설정
                    : "ROLE_USER";

            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header(HEADER_USER_ID, userId)
                    .header(HEADER_EMAIL, email)
                    .header(HEADER_USERNAME, username)
                    .header(HEADER_ROLES, rolesString)
                    .build();

            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
            return chain.filter(mutatedExchange);
        });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private Mono<Void> handleUnauthenticated(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String errorBody = String.format("{\"status\": 401, \"error\": \"Unauthorized\", \"message\": \"%s\"}", message);

        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(errorBody.getBytes(StandardCharsets.UTF_8)))
        );
    }
}
