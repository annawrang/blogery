package com.annawrang.blogery.resource;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class CommentResource {

    private UUID commentId;

    @NotBlank
    private String text;

    private String createdBy;

    private Instant createdAt;
}
