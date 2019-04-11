package com.annawrang.blogery.config;

import com.annawrang.blogery.exception.ForbiddenException;
import com.annawrang.blogery.exception.NotFoundException;
import com.annawrang.blogery.model.Account;
import com.annawrang.blogery.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    private AccountRepository accountRepository;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        Account account = accountRepository.findByEmail(email).orElseThrow(NotFoundException::new);

        if (!account.getPassword().equals(password)) {
            throw new ForbiddenException();
        }
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
