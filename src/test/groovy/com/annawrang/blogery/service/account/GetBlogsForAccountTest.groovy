package com.annawrang.blogery.service.account

import com.annawrang.blogery.exception.ForbiddenException
import com.annawrang.blogery.exception.UnauthorizedException
import com.annawrang.blogery.repository.AccountRepository
import com.annawrang.blogery.repository.BlogRepository
import com.annawrang.blogery.service.AccountService
import com.annawrang.blogery.service.BaseUnitTest

import javax.validation.Validation

class GetBlogsForAccountTest extends BaseUnitTest {

    AccountService target

    AccountRepository accountRepository

    BlogRepository blogRepository

    def setup() {
        accountRepository = Mock(AccountRepository)
        blogRepository = Mock(BlogRepository)
        target = new AccountService(
                accountRepository: accountRepository,
                blogRepository: blogRepository,
                validator: Validation.buildDefaultValidatorFactory().getValidator()
        )
    }

    def 'an exception should be thrown if no user is authenticated'() {
        given:
        setupNoAuth()
        when:
        target.getBlogs(random())
        then:
        thrown(UnauthorizedException)
    }

    def 'an exception should be thrown if the authenticated user does not match accountId provided'() {
        given:
        setupAuth()
        when:
        target.getBlogs(random())
        then:
        thrown(ForbiddenException)
    }

}
