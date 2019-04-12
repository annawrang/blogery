package com.annawrang.blogery.repository;

import com.annawrang.blogery.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends MongoRepository<Post, UUID> {
    Optional<Post> findByBlogIdAndPostId(UUID blogId, UUID postId);
}
