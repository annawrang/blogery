package com.annawrang.blogery.service.blog

import com.annawrang.blogery.exception.UnauthorizedException
import com.annawrang.blogery.repository.BlogRepository
import com.annawrang.blogery.service.AccountService
import com.annawrang.blogery.service.BaseUnitTest
import com.annawrang.blogery.service.BlogService
import spock.lang.Unroll

import javax.validation.ConstraintViolationException
import javax.validation.Validation

class CreateBlogTest extends BaseUnitTest {

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
        def resource = blogResource('name')
        and:
        setupNoAuth()
        when:
        target.createBlog(resource)
        then:
        1 * accountService.getCurrentUserId() >> { throw new UnauthorizedException() }
        0 * _
        thrown(UnauthorizedException)
    }

    @Unroll
    def 'an exception should be thrown if #scenario'() {
        given:
        def resource = blogResource(name, description)
        and: 'auth is setup'
        setupAuth()
        when:
        target.createBlog(resource)
        then:
        thrown(ConstraintViolationException)
        and:
        0 * _
        where:
        name   | scenario                  | description
        null   | 'name is null'            | 'description'
        ''     | 'name is empty'           | 'description'
        'name' | 'description is too long' | 'descriptiondescriptiondescriptiondescriptioondescriptiooondescriptioondescriptioniondescriptiondescriptiondescription'
    }
}
