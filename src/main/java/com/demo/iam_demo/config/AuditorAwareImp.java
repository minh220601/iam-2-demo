package com.demo.iam_demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAware")
public class AuditorAwareImp implements AuditorAware<String> {
    @Value("${auth.keycloak-enable:false}")
    private boolean keycloakEnable;

    @Override
    public Optional<String> getCurrentAuditor(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()
            || authentication.getPrincipal().equals("anonymousUser")){
            return Optional.empty();
        }

        if(keycloakEnable && authentication.getPrincipal() instanceof Jwt jwt){
            // lấy username từ token keycloak
            return Optional.ofNullable(jwt.getClaimAsString("preferred_username"));
        }

        // self-IDP
        return Optional.ofNullable(authentication.getName());
    }
}
