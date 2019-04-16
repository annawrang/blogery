package com.annawrang.blogery.service;

import com.annawrang.blogery.config.JwtTokenProvider;
import com.annawrang.blogery.config.WebSecurityConfig;
import com.annawrang.blogery.exception.BadRequestException;
import com.annawrang.blogery.exception.ForbiddenException;
import com.annawrang.blogery.exception.NotFoundException;
import com.annawrang.blogery.exception.UnauthorizedException;
import com.annawrang.blogery.model.Account;
import com.annawrang.blogery.model.Blog;
import com.annawrang.blogery.repository.AccountRepository;
import com.annawrang.blogery.repository.BlogRepository;
import com.annawrang.blogery.resource.AccountResource;
import com.annawrang.blogery.resource.AuthTokenResource;
import com.annawrang.blogery.resource.BlogResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Component
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private WebSecurityConfig webSecurityConfig;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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
                .setPassword(encoder.encode(resource.getPassword()));
        return convert(accountRepository.save(account));
    }

    public AccountResource editAccount(UUID accountId, AccountResource resource) {
        validateResource(resource, "email");
        validateResource(resource, "password");

        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(NotFoundException::new);

        validateCurrentUser(getCurrentUserId(), account.getAccountId());

        account.setEmail(resource.getEmail())
                .setPassword(encoder.encode(resource.getPassword()));
        return convert(accountRepository.save(account));
    }

    public AuthTokenResource login(final AccountResource resource) {
        if (resource == null || StringUtils.isBlank(resource.getEmail())
                || StringUtils.isBlank(resource.getPassword())) {
            throw new BadRequestException("Email and/or password not provided");
        }
        Account account = accountRepository.findByEmail(resource.getEmail())
                .orElseThrow(NotFoundException::new);

        if(!encoder.matches(resource.getPassword(), account.getPassword())){
            throw new ForbiddenException("Invalid password");
        }

        String jwtToken = jwtTokenProvider.createToken(resource.getEmail(), account);
        return new AuthTokenResource().setJwtToken(jwtToken).setAccountId(account.getAccountId());
    }

    /**
     * A user can delete their own account
     **/
    public void deleteAccount(UUID accountId) {
        UUID currentUser = getCurrentUserId();

        Account account = accountRepository.findByAccountId(accountId).orElseThrow(NotFoundException::new);
        validateCurrentUser(currentUser, account.getAccountId());
        accountRepository.delete(account);
    }

    public List<BlogResource> getBlogs(UUID accountId) {
        UUID currentUser = getCurrentUserId();
        validateCurrentUser(accountId, currentUser);

        List<Blog> blogs = blogRepository.findByAccountId(accountId);

        return blogs.stream().map(this::convert).collect(Collectors.toList());
    }

    private void validateCurrentUser(UUID accountId, UUID currentUser) {
        if (!currentUser.equals(accountId)) {
            throw new ForbiddenException("Authenticated user does not match accountId provided");
        }
    }

    public UUID getCurrentUserId() {
        try {
            return UUID.fromString(getContext().getAuthentication().getName());
        } catch (NullPointerException e) {
            throw new UnauthorizedException("No user is authenticated");
        } catch (IllegalArgumentException e) {
            throw new ForbiddenException("Invalid accountId in refreshToken");
        }
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

    private BlogResource convert(Blog blog) {
        return new BlogResource()
                .setBlogId(blog.getBlogId())
                .setBackgroundPictureUrl(blog.getBackgroundPictureUrl())
                .setCreatedAt(blog.getCreatedAt())
                .setDescription(blog.getDescription())
                .setName(blog.getName())
                .setProfilePictureUrl(blog.getProfilePictureUrl());
    }
}
