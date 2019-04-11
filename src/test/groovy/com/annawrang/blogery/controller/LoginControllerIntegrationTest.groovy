package com.annawrang.blogery.controller

import com.annawrang.blogery.exception.BadRequestException
import com.annawrang.blogery.exception.NotFoundException
import com.annawrang.blogery.resource.AccountResource
import com.annawrang.blogery.resource.AuthTokenResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration
class LoginControllerIntegrationTest extends BaseAccountIntegrationTest {

    @Autowired
    AccountController target

    def 'login should throw exception if account does not exist'() {
        given:
        def resource = accountResource('anna@gmail.com', 'Secret123')
        when:
        target.login(resource)
        then:
        thrown(NotFoundException)
    }

    def 'login should return a jwt token and the accountId'() {
        given:
        def resource = accountResource('anna@gmail.com', 'Secret123')
        and: 'an account exists with that email'
        def account = account('anna@gmail.com', 'Secret123')
        accountRepo.save(account)
        when:
        def result = target.login(resource)
        then:
        result.statusCode.value() == 200
        def body = (AuthTokenResource) result.body
        body.jwtToken != null
        body.accountId != null
    }
}
