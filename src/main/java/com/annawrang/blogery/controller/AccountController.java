package com.annawrang.blogery.controller;

import com.annawrang.blogery.resource.AccountResource;
import com.annawrang.blogery.resource.AuthTokenResource;
import com.annawrang.blogery.resource.BlogResource;
import com.annawrang.blogery.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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


    @PutMapping(path = "/{accountId}")
    public ResponseEntity editAccount(@PathVariable("accountId") final UUID accountId,
                                      @RequestBody final AccountResource resource) {
        AccountResource account = accountService.editAccount(accountId, resource);
        return ResponseEntity.ok(account);
    }

    @PostMapping(path = "/login")
    public ResponseEntity login(@RequestBody final AccountResource resource) {
        AuthTokenResource authTokens = accountService.login(resource);
        return ResponseEntity.ok(authTokens);
    }

//  TODO test last
    @DeleteMapping(value = "/{accountId}")
    public ResponseEntity deleteAccount(@PathVariable("accountId") final UUID accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{accountId}/blogs")
    public ResponseEntity getBlogsForAccount(@PathVariable("accountId") final UUID accountId) {
        List<BlogResource> blogs = accountService.getBlogs(accountId);
        return ResponseEntity.ok(blogs);
    }
}
