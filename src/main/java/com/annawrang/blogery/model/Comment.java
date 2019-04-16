package com.annawrang.blogery.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class Comment {

    @Id
    private UUID commentId;

    private String text;

    private String createdBy;

    private Instant createdAt;
}
