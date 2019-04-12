package com.annawrang.blogery.service.blog

import com.annawrang.blogery.exception.UnauthorizedException
import com.annawrang.blogery.repository.BlogRepository
import com.annawrang.blogery.repository.PostRepository
import com.annawrang.blogery.service.AccountService
import com.annawrang.blogery.service.BaseUnitTest
import com.annawrang.blogery.service.BlogService
import spock.lang.Unroll

import javax.validation.ConstraintViolationException
import javax.validation.Validation

class DeleteBlogPostTest extends BaseUnitTest{
    BlogService target

    BlogRepository blogRepository

    PostRepository postRepository

    AccountService accountService

    def setup() {
        accountService = Mock(AccountService)
        blogRepository = Mock(BlogRepository)
        postRepository = Mock(PostRepository)
        target = new BlogService(
                accountService: accountService,
                blogRepository: blogRepository,
                postRepository: postRepository,
                validator: Validation.buildDefaultValidatorFactory().getValidator()
        )
    }

    def 'an exception should be thrown if no user is authenticated'() {
        given:
        setupNoAuth()
        when:
        target.deletePost(random(), random())
        then:
        1 * accountService.getCurrentUserId() >> { throw new UnauthorizedException() }
        0 * _
        thrown(UnauthorizedException)
    }
}
