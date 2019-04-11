package com.annawrang.blogery.controller.blog


import com.annawrang.blogery.repository.BlogRepository
import com.annawrang.blogery.service.blog.BaseBlogTest
import org.springframework.beans.factory.annotation.Autowired

class BaseBlogIntegrationTest extends BaseBlogTest {


    @Autowired
    BlogRepository blogRepo

    def setup() {
        assert blogRepo.count() == 0
    }

    def cleanup() {
        blogRepo.deleteAll()
    }
}
