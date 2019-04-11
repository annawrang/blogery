package com.annawrang.blogery.service;

import com.annawrang.blogery.exception.BadRequestException;
import com.annawrang.blogery.exception.ForbiddenException;
import com.annawrang.blogery.exception.NotFoundException;
import com.annawrang.blogery.model.Blog;
import com.annawrang.blogery.repository.BlogRepository;
import com.annawrang.blogery.resource.BlogResource;
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
    private AccountService accountService;

    @Autowired
    private Validator validator;

    public BlogResource createBlog(BlogResource resource) {

        validateResource(resource, "name");
        validateResource(resource, "description");

        UUID currentUser = accountService.getCurrentUserId();

        if(blogRepository.findByName(resource.getName()).isPresent()){
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

        if(!blog.getAccountId().equals(currentUser)){
            throw new ForbiddenException("The user does not have the rights to perform this action");
        }

        blogRepository.delete(blog);
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

    private void validateResource(final BlogResource resource, final String property) {
        final Set<ConstraintViolation<BlogResource>> violations = validator.validateProperty(resource, property);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
