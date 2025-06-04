package com.alten.back.utils;

import com.alten.back.security.model.ExtraUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

public class CustomSecurityContextFactory implements WithSecurityContextFactory<WithMockAccountUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockAccountUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        ExtraUserDetails principal = new ExtraUserDetails(
                customUser.username(),
                customUser.password(),
                customUser.email(),
                Collections.emptyList()
        );

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal, principal.getPassword(), principal.getAuthorities());

        context.setAuthentication(authentication);
        return context;
    }
}
