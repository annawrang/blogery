package com.annawrang.blogery.service.account


import com.annawrang.blogery.exception.ForbiddenException
import com.annawrang.blogery.exception.UnauthorizedException
import com.annawrang.blogery.repository.AccountRepository
import com.annawrang.blogery.service.AccountService

import javax.validation.Validation

class DeleteAccountTest extends BaseAccountTest {

    AccountService target

    AccountRepository accountRepository

    def setup() {
        accountRepository = Mock(AccountRepository)
        target = new AccountService(
                accountRepository: accountRepository,
                validator: Validation.buildDefaultValidatorFactory().getValidator()
        )
    }

    def 'an exception is thrown if the user is not authorized'() {
        given:
        setupNoAuth()
        when:
        target.deleteAccount(random())
        then:
        thrown(UnauthorizedException)
    }

    def 'an exception is thrown if the user authenticated does not match the provided accountId'() {
        given:
        setupAuth()
        and:
        def account = account()
        when:
        target.deleteAccount(random())
        then:
        1 * accountRepository.findByAccountId(_ as UUID) >> Optional.of(account)
        and:
        thrown(ForbiddenException)
    }
}
