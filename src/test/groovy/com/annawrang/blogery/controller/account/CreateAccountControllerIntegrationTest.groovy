package com.annawrang.blogery.controller.account

import com.annawrang.blogery.controller.AccountController
import com.annawrang.blogery.exception.BadRequestException
import com.annawrang.blogery.resource.AccountResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration
class CreateAccountControllerIntegrationTest extends BaseAccountIntegrationTest {

    @Autowired
    AccountController target

    def 'createAccount should throw exception if email is already registered'() {
        given:
        def resource = accountResource('anna@gmail.com', 'Secret123')
        and: 'an account exists with that email'
        def account = account('anna@gmail.com', 'Secret123')
        accountRepo.save(account)
        when:
        target.signup(resource)
        then:
        thrown(BadRequestException)
    }

    def 'createAccount should return the created resource'() {
        given:
        def resource = accountResource('anna@gmail.com', 'Secret123')
        when:
        def result = target.signup(resource)
        then:
        result.statusCode.value() == 201
        def body = (AccountResource) result.body
        body.email == resource.email
        body.password == null
        body.accountId != null
    }
}
