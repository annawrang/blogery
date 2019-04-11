package com.annawrang.blogery.config;

import com.annawrang.blogery.exception.NotFoundException;
import com.annawrang.blogery.model.Account;
import com.annawrang.blogery.repository.AccountRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.refreshToken.secret-key:secret-key}")
    private String secretKey;

    @Value("${security.jwt.refreshToken.expire-length:3600000}")
    private long validityInMilliseconds = 10; // 1h

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String email, Account account) {
        Claims claims = Jwts.claims().setSubject(account.getEmail());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()//
                .setSubject(email)
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .compact();
    }
}
