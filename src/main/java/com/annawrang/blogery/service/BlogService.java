package com.annawrang.blogery.service;

import com.annawrang.blogery.exception.BadRequestException;
import com.annawrang.blogery.exception.ForbiddenException;
import com.annawrang.blogery.exception.NotFoundException;
import com.annawrang.blogery.model.Blog;
import com.annawrang.blogery.model.Comment;
import com.annawrang.blogery.model.Post;
import com.annawrang.blogery.repository.BlogRepository;
import com.annawrang.blogery.repository.PostRepository;
import com.annawrang.blogery.resource.BlogResource;
import com.annawrang.blogery.resource.CommentResource;
import com.annawrang.blogery.resource.PostResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AmazonClient amazonClient;

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
        postRepository.deleteAllByBlogId(blogId);
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
                .setComments(new ArrayList<>())
                .setUrls(resource.getUrls());

        return convert(postRepository.save(post));
    }

    public PostResource getPost(UUID blogId, UUID postId) {
        return convert(postRepository.findByBlogIdAndPostId(blogId, postId).orElseThrow(NotFoundException::new));
    }

    public PostResource editPost(UUID blogId, UUID postId, PostResource resource) {
        UUID currentUser = accountService.getCurrentUserId();
        validateResource(resource, "title");
        Blog blog = blogRepository.findByBlogId(blogId).orElseThrow(NotFoundException::new);
        validateBlogOwner(currentUser, blog.getAccountId());

        Post post = postRepository.findByBlogIdAndPostId(blogId, postId).orElseThrow(NotFoundException::new);

        post.setText(resource.getText())
                .setTitle(resource.getTitle())
                .setUrls(resource.getUrls());
        return convert(postRepository.save(post));
    }

    public void deletePost(UUID blogId, UUID postId) {
        UUID currentUser = accountService.getCurrentUserId();
        Blog blog = blogRepository.findByBlogId(blogId).orElseThrow(NotFoundException::new);
        validateBlogOwner(currentUser, blog.getAccountId());

        Post post = postRepository.findByBlogIdAndPostId(blogId, postId).orElseThrow(NotFoundException::new);
        postRepository.delete(post);
    }

    public PagedResources<PostResource> getPosts(UUID blogId, Pageable pageable) {
        Page<Post> posts = postRepository.findAllByBlogId(blogId, pageable);
        List<PostResource> resources = posts.stream().map(this::convert).collect(Collectors.toList());

        return new PagedResources<>(resources, new PagedResources.PageMetadata(
                pageable.getPageSize(), pageable.getPageNumber(), posts.getTotalElements()));
    }

    public String uploadContent(final MultipartFile file) {
//        accountService.getCurrentUserId();
        return amazonClient.uploadFile(file);
    }

    public CommentResource createComment(UUID blogId, UUID postId, CommentResource resource) {
        validateResource(resource, "text");
        Post post = postRepository.findByBlogIdAndPostId(blogId, postId).orElseThrow(NotFoundException::new);

        Comment comment = new Comment()
                .setCommentId(UUID.randomUUID())
                .setCreatedAt(Instant.now())
                .setText(resource.getText())
                .setCreatedBy(resource.getCreatedBy() == null ? "Anonymous" : resource.getCreatedBy());
        postRepository.save(post.addComment(comment));
        return convert(comment);
    }

    public void deleteComment(UUID blogId, UUID postId, UUID commentId) {
        UUID currentUser = accountService.getCurrentUserId();
        Blog blog = blogRepository.findByBlogId(blogId).orElseThrow(NotFoundException::new);
        validateBlogOwner(currentUser, blog.getAccountId());

        Post post = postRepository.findByBlogIdAndPostId(blogId, postId).orElseThrow(NotFoundException::new);
        Comment comment = post.getComments().stream()
                .filter(c -> c.getCommentId().equals(commentId)).findAny().orElseThrow(NotFoundException::new);
        post.getComments().remove(comment);
        postRepository.save(post);
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
                .setUrls(post.getUrls())
                .setComments(post.getComments().stream().map(this::convert).collect(Collectors.toList()));
    }

    private CommentResource convert(Comment comment) {
        return new CommentResource()
                .setCommentId(comment.getCommentId())
                .setCreatedAt(comment.getCreatedAt())
                .setText(comment.getText())
                .setCreatedBy(comment.getCreatedBy());
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

    private void validateResource(final CommentResource resource, final String property) {
        final Set<ConstraintViolation<CommentResource>> violations = validator.validateProperty(resource, property);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public void deleteBlogsByAccount(UUID accountId) {
        List<Blog> blogs = blogRepository.findByAccountId(accountId);
        blogs.forEach(b -> {
            postRepository.deleteAllByBlogId(b.getBlogId());
            deleteBlog(b.getBlogId());
        });
    }
}
