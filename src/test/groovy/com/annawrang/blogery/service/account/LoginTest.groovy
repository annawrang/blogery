package com.annawrang.blogery.service.account

import com.annawrang.blogery.BaseAccountTest
import com.annawrang.blogery.exception.BadRequestException
import com.annawrang.blogery.repository.AccountRepository
import com.annawrang.blogery.service.AccountService
import spock.lang.Unroll

import javax.validation.ConstraintViolationException
import javax.validation.Validation

class LoginTest extends BaseAccountTest {

    AccountService target

    AccountRepository accountRepository

    def setup() {
        accountRepository = Mock(AccountRepository)
        target = new AccountService(
                accountRepository: accountRepository,
                validator: Validation.buildDefaultValidatorFactory().getValidator()
        )
    }

    @Unroll
    def 'login should throw an exception if the #scenario'() {
        given:
        def resource = accountResource(email, password)
        when:
        target.login(resource)
        then:
        thrown(BadRequestException)
        where:
        email            | password    | scenario
        null             | 'Secret123' | 'email is null'
        ''               | 'Secret123' | 'email is empty'
        'anna@gmail.com' | null        | 'password is null'
        'anna@gmail.com' | null        | 'password is empty'
    }
}
