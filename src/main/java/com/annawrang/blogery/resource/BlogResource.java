package com.annawrang.blogery.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogResource {

    private UUID blogId;

    @NotBlank
    private String name;

    @Length(max = 100)

    private String description;

    private String backgroundPictureUrl;

    private String profilePictureUrl;

    private Instant createdAt;
}
