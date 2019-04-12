package com.annawrang.blogery.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class Post {

    private UUID postId;

    private UUID blogId;

    private Instant createdAt;

    private String title;

    private String text;

    private List<String> urls;
}

