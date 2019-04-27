package com.annawrang.blogery.repository;

import com.annawrang.blogery.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends MongoRepository<Post, UUID> {
    Optional<Post> findByBlogIdAndPostId(UUID blogId, UUID postId);

    Page<Post> findAllByBlogId(UUID blogId, Pageable pageable);

    void deleteAllByBlogId(UUID blogId);
}
