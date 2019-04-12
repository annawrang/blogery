package com.annawrang.blogery.resource;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class PostResource {

    private UUID postId;

    private Instant createdAt;

    @NotBlank
    private String title;

    private String text;

    private List<String> urls;
}
