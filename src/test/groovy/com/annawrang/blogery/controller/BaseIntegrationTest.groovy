package com.annawrang.blogery.controller

import com.annawrang.blogery.repository.AccountRepository
import com.annawrang.blogery.repository.BlogRepository
import com.annawrang.blogery.repository.PostRepository
import com.annawrang.blogery.service.BaseUnitTest
import org.springframework.beans.factory.annotation.Autowired

class BaseIntegrationTest extends BaseUnitTest {

    @Autowired
    BlogRepository blogRepo

    @Autowired
    AccountRepository accountRepo

    @Autowired
    PostRepository postRepo

    def setup() {
        assert blogRepo.count() == 0
        assert accountRepo.count() == 0
        assert postRepo.count() == 0
    }

    def cleanup() {
        blogRepo.deleteAll()
        accountRepo.deleteAll()
        postRepo.deleteAll()
    }
}
