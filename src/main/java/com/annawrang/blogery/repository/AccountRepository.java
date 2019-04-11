package com.annawrang.blogery.repository;

import com.annawrang.blogery.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends MongoRepository<Account, UUID> {
    Optional<Account> findByEmail(final String email);

    Optional<Account> findByAccountId(UUID accountId);

    Optional<Account> findByEmailAndPassword(String email, String password);
}
