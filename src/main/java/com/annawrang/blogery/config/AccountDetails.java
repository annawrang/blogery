package com.annawrang.blogery.config;

import com.annawrang.blogery.exception.ForbiddenException;
import com.annawrang.blogery.exception.NotFoundException;
import com.annawrang.blogery.model.Account;
import com.annawrang.blogery.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AccountDetails implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

//    public Account getAccountByEmail(String email) {
//        return accountRepository.findByEmail(email).orElseThrow(NotFoundException::new);
//    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(ForbiddenException::new);

        List<GrantedAuthority> grantedAuths = new ArrayList<>();

        return org.springframework.security.core.userdetails.User
                .withUsername(account.getAccountId().toString())
                .password(account.getPassword())
                .authorities(grantedAuths)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}