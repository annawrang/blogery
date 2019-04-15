package com.annawrang.blogery.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class Account {
    @Id
    private UUID accountId = UUID.randomUUID();

    @Indexed(unique = true)
    private String email;

    private String password;
}
