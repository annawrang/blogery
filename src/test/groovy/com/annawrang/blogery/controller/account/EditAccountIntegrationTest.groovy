package com.annawrang.blogery.controller.account

import com.annawrang.blogery.controller.AccountController
import com.annawrang.blogery.controller.BaseIntegrationTest
import com.annawrang.blogery.exception.ForbiddenException
import com.annawrang.blogery.exception.NotFoundException
import com.annawrang.blogery.exception.UnauthorizedException
import com.annawrang.blogery.resource.AccountResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration
class EditAccountIntegrationTest extends BaseIntegrationTest {

    @Autowired
    AccountController target

    def 'an exception should be thrown if the account does not exist'() {
        given:
        def resource = accountResource('new@gmail.com', 'Secret123New')
        and:
        setupAuth()
        when:
        target.editAccount(random(), resource)
        then:
        thrown(NotFoundException)
    }

    def 'an exception should be thrown if no user is authenticated'() {
        given:
        def resource = accountResource('new@gmail.com', 'Secret123New')
        and:
        and:
        def account = account()
        account = accountRepo.save(account)
        setupNoAuth()
        when:
        target.editAccount(account.accountId, resource)
        then:
        thrown(UnauthorizedException)
    }

    def 'an exception should be thrown if the user does not own the account'() {
        given:
        def resource = accountResource('new@gmail.com', 'Secret123New')
        and:
        and:
        def account = account()
        account = accountRepo.save(account)
        setupAuth(random())
        when:
        target.editAccount(account.accountId, resource)
        then:
        thrown(ForbiddenException)
    }

    def 'createAccount should return the created resource'() {
        given:
        def resource = accountResource('new@gmail.com', 'Secret123New')
        and:
        def account = account()
        account = accountRepo.save(account)
        and:
        setupAuth(account.accountId)
        when:
        def result = target.editAccount(account.accountId, resource)
        then:
        result.statusCode.value() == 200
        def body = (AccountResource) result.body
        body.email == resource.email
        body.password == null
        body.accountId != null
    }
}
