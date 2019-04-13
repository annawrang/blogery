package com.annawrang.blogery.service;

import com.annawrang.blogery.exception.BadRequestException;
import com.annawrang.blogery.exception.ForbiddenException;
import com.annawrang.blogery.exception.NotFoundException;
import com.annawrang.blogery.model.Blog;
import com.annawrang.blogery.model.Post;
import com.annawrang.blogery.repository.BlogRepository;
import com.annawrang.blogery.repository.PostRepository;
import com.annawrang.blogery.resource.BlogResource;
import com.annawrang.blogery.resource.PostResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Component
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private Validator validator;

    public BlogResource createBlog(BlogResource resource) {

        validateResource(resource, "name");
        validateResource(resource, "description");

        UUID currentUser = accountService.getCurrentUserId();

        if (blogRepository.findByName(resource.getName()).isPresent()) {
            throw new BadRequestException("The blog name is already taken");
        }

        Blog blog = new Blog()
                .setBlogId(UUID.randomUUID())
                .setAccountId(currentUser)
                .setName(resource.getName())
                .setBackgroundPictureUrl(resource.getBackgroundPictureUrl())
                .setCreatedAt(Instant.now())
                .setDescription(resource.getDescription())
                .setProfilePictureUrl(resource.getProfilePictureUrl());

        return convert(blogRepository.save(blog));
    }

    public void deleteBlog(UUID blogId) {
        UUID currentUser = accountService.getCurrentUserId();

        Blog blog = blogRepository.findByBlogId(blogId).orElseThrow(NotFoundException::new);

        if (!blog.getAccountId().equals(currentUser)) {
            throw new ForbiddenException("The user does not have the rights to perform this action");
        }

        blogRepository.delete(blog);
    }

    public PostResource createBlogPost(final UUID blogId, final PostResource resource) {
        UUID currentUser = accountService.getCurrentUserId();
        validateResource(resource, "title");
        Blog blog = blogRepository.findByBlogId(blogId).orElseThrow(NotFoundException::new);
        validateBlogOwner(currentUser, blog.getAccountId());

        Post post = new Post()
                .setPostId(UUID.randomUUID())
                .setBlogId(blogId)
                .setCreatedAt(Instant.now())
                .setText(resource.getText())
                .setTitle(resource.getTitle())
                .setUrls(resource.getUrls());

        return convert(postRepository.save(post));
    }

    public PostResource getPost(UUID blogId, UUID postId) {
        Post post = postRepository.findByBlogIdAndPostId(blogId, postId).orElseThrow(NotFoundException::new);

        return convert(post);
    }

    public void deletePost(UUID blogId, UUID postId) {
        UUID currentUser = accountService.getCurrentUserId();
        Blog blog = blogRepository.findByBlogId(blogId).orElseThrow(NotFoundException::new);
        validateBlogOwner(currentUser, blog.getAccountId());

        Post post = postRepository.findByBlogIdAndPostId(blogId, postId).orElseThrow(NotFoundException::new);
        postRepository.delete(post);
    }

    private void validateBlogOwner(UUID currentUser, UUID accountId) {
        if (!currentUser.equals(accountId)) {
            throw new ForbiddenException("The user does not own the blog");
        }
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

    private PostResource convert(Post post) {
        return new PostResource()
                .setCreatedAt(post.getCreatedAt())
                .setPostId(post.getPostId())
                .setText(post.getText())
                .setTitle(post.getTitle())
                .setUrls(post.getUrls());
    }

    private void validateResource(final BlogResource resource, final String property) {
        final Set<ConstraintViolation<BlogResource>> violations = validator.validateProperty(resource, property);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void validateResource(final PostResource resource, final String property) {
        final Set<ConstraintViolation<PostResource>> violations = validator.validateProperty(resource, property);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
