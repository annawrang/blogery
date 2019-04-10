package com.annawrang.blogery.controller

import com.annawrang.blogery.BaseAccountTest
import com.annawrang.blogery.repository.AccountRepository
import org.springframework.beans.factory.annotation.Autowired

class BaseAccountIntegrationTest extends BaseAccountTest {

    @Autowired
    AccountRepository accountRepo

    def setup() {
        assert accountRepo.count() == 0
    }

    def cleanup() {
        accountRepo.deleteAll()
    }
}
