package com.annawrang.blogery.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class Post {

    @Id
    private UUID postId;

    private UUID blogId;

    private Instant createdAt;

    private String title;

    private String text;

    private List<String> urls;

    private List<Comment> comments;

    public Post addComment(final Comment comment){
        if(comments == null){
            comments = new ArrayList<>();
        }
        comments.add(comment);
        return this;
    }
}

