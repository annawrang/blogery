package com.annawrang.blogery.service;

import com.annawrang.blogery.config.WebSecurityConfig;
import com.annawrang.blogery.exception.BadRequestException;
import com.annawrang.blogery.model.Account;
import com.annawrang.blogery.repository.AccountRepository;
import com.annawrang.blogery.resource.AccountResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;
import java.util.UUID;

@Component
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WebSecurityConfig webSecurityConfig;

    @Autowired
    private Validator validator;

    /**
     * Sign up - needs email and password provided
     **/
    public AccountResource createAccount(final AccountResource resource) {
        validateResource(resource, "email");
        validateResource(resource, "password");

        accountRepository.findByEmail(resource.getEmail())
                .ifPresent(p -> {
                    throw new BadRequestException("Email address is already registered");
                });

        Account account = new Account()
                .setAccountId(UUID.randomUUID())
                .setEmail(resource.getEmail())
                .setPassword(webSecurityConfig.passwordEncoder().encode(resource.getPassword()));
        return convert(accountRepository.save(account));
    }

    private void validateResource(final AccountResource resource, final String property) {
        final Set<ConstraintViolation<AccountResource>> violations = validator.validateProperty(resource, property);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private AccountResource convert(Account account) {
        return new AccountResource()
                .setAccountId(account.getAccountId())
                .setEmail(account.getEmail());
    }
}
