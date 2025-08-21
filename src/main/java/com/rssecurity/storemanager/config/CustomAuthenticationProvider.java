package com.rssecurity.storemanager.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.rssecurity.storemanager.service.UsuarioService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UsuarioService usuarioService;
    private final PasswordEncoder pswEncoder;

    public CustomAuthenticationProvider(UsuarioService usuarioService, PasswordEncoder pswEncoder) {
        this.usuarioService = usuarioService;
        this.pswEncoder = pswEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = usuarioService.loadUserByUsername(username);

        if (!pswEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Senha incorreta");
        }
        if (!userDetails.isEnabled()) {
            throw new DisabledException("Usuário não autorizado.");
        }
        
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
}
