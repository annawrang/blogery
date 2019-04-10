package com.annawrang.blogery.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class Account {
    @Id
    private UUID accountId = UUID.randomUUID();

    private String email;

    private String password;
}
