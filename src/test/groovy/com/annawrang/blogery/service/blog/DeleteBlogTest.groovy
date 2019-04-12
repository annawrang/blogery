package com.annawrang.blogery.service.blog

import com.annawrang.blogery.exception.UnauthorizedException
import com.annawrang.blogery.repository.BlogRepository
import com.annawrang.blogery.service.AccountService
import com.annawrang.blogery.service.BaseUnitTest
import com.annawrang.blogery.service.BlogService

import javax.validation.Validation

class DeleteBlogTest extends BaseUnitTest {

    BlogService target

    BlogRepository blogRepository

    AccountService accountService

    def setup() {
        accountService = Mock(AccountService)
        blogRepository = Mock(BlogRepository)
        target = new BlogService(
                accountService: accountService,
                blogRepository: blogRepository,
                validator: Validation.buildDefaultValidatorFactory().getValidator()
        )
    }

    def 'an exception should be thrown if no user is authenticated'() {
        given:
        setupNoAuth()
        when:
        target.deleteBlog(random())
        then:
        1 * accountService.getCurrentUserId() >> { throw new UnauthorizedException() }
        0 * _
        thrown(UnauthorizedException)
    }
}
