package com.annawrang.blogery.service.account


import com.annawrang.blogery.repository.AccountRepository
import com.annawrang.blogery.service.AccountService
import com.annawrang.blogery.service.BaseUnitTest
import spock.lang.Unroll

import javax.validation.ConstraintViolationException
import javax.validation.Validation

class CreateAccountTest extends BaseUnitTest {

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
    def 'createAccount should throw an exception if the #scenario'() {
        given:
        def resource = accountResource(email, password)
        when:
        target.createAccount(resource)
        then:
        thrown(ConstraintViolationException)
        where:
        email            | password     | scenario
        null             | 'Secret123'  | 'email is null'
        ''               | 'Secret123'  | 'email is empty'
        'invalid.email'  | 'Secret123'  | 'email is invalid format'
        'anna@gmail.com' | null         | 'password is null'
        'anna@gmail.com' | 'secret1234' | 'password is missing upper case'
        'anna@gmail.com' | 'Secret1'    | 'password is too short'
    }
}
