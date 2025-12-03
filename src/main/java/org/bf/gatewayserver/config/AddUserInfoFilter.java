//package org.bf.gatewayserver.config;
//
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.security.core.context.ReactiveSecurityContextHolder;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//
//@Component
//public class AddUserInfoFilter implements GlobalFilter, Ordered {
//
//    private static final String HEADER_USER_ID = "X-User-Id";   // 사용자 식별 ID
//    private static final String HEADER_USERNAME = "X-Username"; // 로그인 아이디
//    private static final String HEADER_EMAIL = "X-User-Email";  // 이메일
//    private static final String HEADER_NICKNAME = "X-Nickname"; // 닉네임
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        return ReactiveSecurityContextHolder.getContext()
//                .map(ctx -> ctx == null ? null : ctx.getAuthentication())
//                .flatMap(auth -> {
//                    if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
//                        // 인증이 없거나 Jwt가 아니면 그냥 통과
//                        return chain.filter(exchange);
//                    }
//
//                    Jwt jwt = jwtAuth.getToken();
//
//                    String userId = Optional.ofNullable(jwt.getClaimAsString("user_id"))
//                            .orElseGet(() -> Optional.ofNullable(jwt.getSubject()).orElse(""));
//
//                    String username = Optional.ofNullable(jwt.getClaimAsString("username")).orElse("");
//                    String email = Optional.ofNullable(jwt.getClaimAsString("email")).orElse("");
//                    String nickname = Optional.ofNullable(jwt.getClaimAsString("nickname")).orElse(username);
//                    String encodedNickname = URLEncoder.encode(nickname, StandardCharsets.UTF_8);
//
//                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
//                            .header(HEADER_USER_ID, userId)
//                            .header(HEADER_USERNAME, username)
//                            .header(HEADER_EMAIL, email)
//                            .header(HEADER_NICKNAME, encodedNickname)
//                            .build();
//
//                    ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
//                    return chain.filter(mutatedExchange);
//                })
//                .switchIfEmpty(chain.filter(exchange))
//                .onErrorResume(ex -> {
//                    return chain.filter(exchange);
//                });
//    }
//
//    @Override
//    public int getOrder() {
//        return Ordered.HIGHEST_PRECEDENCE;
//    }
//}
