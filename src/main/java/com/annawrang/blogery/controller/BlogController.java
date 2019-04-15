package com.annawrang.blogery.controller;

import com.annawrang.blogery.resource.AccountResource;
import com.annawrang.blogery.resource.BlogResource;
import com.annawrang.blogery.resource.PostResource;
import com.annawrang.blogery.service.AccountService;
import com.annawrang.blogery.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @DeleteMapping(path = "/{blogId}")
    public ResponseEntity deleteBlog(@PathVariable final UUID blogId) {
        blogService.deleteBlog(blogId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping(path = "/{blogId}/posts")
    public ResponseEntity createBlogPost(@PathVariable final UUID blogId,
                                         @RequestBody final PostResource resource) {
        PostResource savedPost = blogService.createBlogPost(blogId, resource);
        return ResponseEntity.status(201).body(savedPost);
    }

    @DeleteMapping(path = "/{blogId}/posts/{postId}")
    public ResponseEntity deletePost(@PathVariable final UUID blogId,
                                         @PathVariable final UUID postId) {
        blogService.deletePost(blogId, postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{blogId}/posts/{postId}")
    public ResponseEntity getPost(@PathVariable final UUID blogId,
                                         @PathVariable final UUID postId) {
        PostResource post = blogService.getPost(blogId, postId);
        return ResponseEntity.ok(post);
    }

    @PutMapping(path = "/{blogId}/posts/{postId}")
    public ResponseEntity editPost(@PathVariable final UUID blogId,
                                         @PathVariable final UUID postId,
                                   @RequestBody final PostResource resource) {
        PostResource post = blogService.editPost(blogId, postId, resource);
        return ResponseEntity.ok(post);
    }

    @GetMapping(path = "/{blogId}/posts")
    public ResponseEntity getAllPosts(@PathVariable final UUID blogId,
                                      Pageable pageable) {
        PagedResources<PostResource> post = blogService.getPosts(blogId, pageable);
        return ResponseEntity.ok(post);
    }
}
