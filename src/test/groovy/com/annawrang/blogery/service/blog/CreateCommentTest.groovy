package com.annawrang.blogery.service.blog

import com.annawrang.blogery.exception.NotFoundException
import com.annawrang.blogery.repository.BlogRepository
import com.annawrang.blogery.repository.PostRepository
import com.annawrang.blogery.service.AccountService
import com.annawrang.blogery.service.BaseUnitTest
import com.annawrang.blogery.service.BlogService
import spock.lang.Unroll

import javax.validation.ConstraintViolationException
import javax.validation.Validation

class CreateCommentTest extends BaseUnitTest{
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

    @Unroll
    def 'an exception should be thrown if text is invalid'() {
        given:
        def resource = commentResource(random(), text)
        and:
        setupAuth()
        when:
        target.createComment(random(), random(), resource)
        then:
        0 * _
        thrown(ConstraintViolationException)
        where:
        text | scenario
        null  | 'is null'
        ''    | 'is blank'
    }
}
