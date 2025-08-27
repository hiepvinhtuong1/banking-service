//package com.tuanhiep.banking_service.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.crypto.spec.SecretKeySpec;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    // các endpoint không cần phải kiểm tra
//    private final String[] publicEndpoints = {
//            "/auth/**",
//    };
//
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        // Cấu hình endpoint cần bảo vệ
//        httpSecurity.authorizeHttpRequests(request ->
//                request.requestMatchers(HttpMethod.POST,publicEndpoints).permitAll()
//                        .anyRequest().authenticated());
//
//        httpSecurity.oauth2ResourceServer(oauth2 ->
//                oauth2.jwt(jwt -> jwt.decoder(customJwtDecoder())));
//
//        // Tắt csrf
//        httpSecurity.csrf(AbstractHttpConfigurer::disable);
//
//
//        return httpSecurity.build();
//    }
//
//    @Bean
//    JwtDecoder customJwtDecoder(){
//        SecretKeySpec secretKeySpec = new SecretKeySpec("")
//        return NimbusJwtDecoder.withSecretKey();
//    }
//
//}
