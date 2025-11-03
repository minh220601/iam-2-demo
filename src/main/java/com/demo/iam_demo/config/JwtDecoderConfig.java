package com.demo.iam_demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtDecoderConfig {
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean("keycloakDecoder")
    public JwtDecoder keycloakDecoder(){
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }

    @Bean("selfIdpDecoder")
    public JwtDecoder selfIdpDecoder(){
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "HmacSHA512");
        return NimbusJwtDecoder.withSecretKey(keySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }
}
