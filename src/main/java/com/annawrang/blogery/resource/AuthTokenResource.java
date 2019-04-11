package com.annawrang.blogery.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthTokenResource {

    private String jwtToken;

    private UUID accountId;
}
