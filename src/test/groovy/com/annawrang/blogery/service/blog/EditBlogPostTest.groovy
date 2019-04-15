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

class EditBlogPostTest extends BaseUnitTest {

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
        def resource = postResource()
        and:
        setupNoAuth()
        when:
        target.editPost(random(), random(), resource)
        then:
        1 * accountService.getCurrentUserId() >> { throw new UnauthorizedException() }
        0 * _
        thrown(UnauthorizedException)
    }

    @Unroll
    def 'an exception should be thrown if title is invalid'() {
        given:
        def resource = postResource(random(), title)
        and:
        setupAuth()
        when:
        target.editPost(random(), random(), resource)
        then:
        1 * accountService.getCurrentUserId() >> { random() }
        0 * _
        thrown(ConstraintViolationException)
        where:
        title | scenario
        null  | 'is null'
        ''    | 'is blank'
    }
}
