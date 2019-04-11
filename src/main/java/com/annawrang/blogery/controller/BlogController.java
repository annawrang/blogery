package com.annawrang.blogery.controller;

import com.annawrang.blogery.resource.AccountResource;
import com.annawrang.blogery.resource.BlogResource;
import com.annawrang.blogery.service.AccountService;
import com.annawrang.blogery.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @PostMapping
    public ResponseEntity createBlog(@RequestBody final BlogResource resource) {
        BlogResource createdBlog = blogService.createBlog(resource);
        return ResponseEntity.status(201).body(createdBlog);
    }
}
