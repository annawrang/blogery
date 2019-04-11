package com.annawrang.blogery.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class Blog {

    @Id
    private UUID blogId = UUID.randomUUID();

    private String name;

    private String description;

    private UUID accountId;

    private String profilePictureUrl;

    private String backgroundPictureUrl;

    private Instant createdAt;
}
