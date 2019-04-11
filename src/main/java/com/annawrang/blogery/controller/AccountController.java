package com.annawrang.blogery.controller;

import com.annawrang.blogery.resource.AccountResource;
import com.annawrang.blogery.resource.AuthTokenResource;
import com.annawrang.blogery.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity signup(@RequestBody final AccountResource resource) {
        AccountResource createdAccount = accountService.createAccount(resource);
        return ResponseEntity.status(201).body(createdAccount);
    }

    @PostMapping(path = "/login")
    public ResponseEntity login(@RequestBody final AccountResource resource) {
        AuthTokenResource authTokens = accountService.login(resource);
        return ResponseEntity.ok(authTokens);
    }

    public ResponseEntity deleteAccount(@PathVariable("accountId") final UUID accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}
