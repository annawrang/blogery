package com.annawrang.blogery.repository;

import com.annawrang.blogery.model.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface BlogRepository extends MongoRepository<Blog, UUID> {
    Optional<Blog> findByName(String name);

    Optional<Blog> findByBlogId(UUID blogId);
}
