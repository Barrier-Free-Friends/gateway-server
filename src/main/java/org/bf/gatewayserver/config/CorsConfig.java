package org.bf.gatewayserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class CorsConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 모든 출처(*) 허용: HTTP/HTTPS 및 모든 도메인 허용
                .allowedOrigins("*")

                // 모든 HTTP 메서드 허용
                .allowedMethods("*")

                // 모든 헤더 허용
                .allowedHeaders("*")

                // 자격 증명(쿠키, 인증 헤더) 허용 여부.
                .allowCredentials(false)

                // preflight 요청 캐시 시간
                .maxAge(3600);
    }
}
