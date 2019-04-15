package com.annawrang.blogery.controller.account

import com.annawrang.blogery.controller.AccountController
import com.annawrang.blogery.controller.BaseIntegrationTest
import com.annawrang.blogery.exception.ForbiddenException
import com.annawrang.blogery.exception.NotFoundException
import com.annawrang.blogery.resource.AuthTokenResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration
class LoginControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    AccountController target

    @Autowired
    BCryptPasswordEncoder encoder

    def 'login should throw exception if account does not exist'() {
        given:
        def resource = accountResource('anna@gmail.com', 'Secret123')
        when:
        target.login(resource)
        then:
        thrown(NotFoundException)
    }

    def 'login should throw an exception if password is wrong'() {
        given:
        def resource = accountResource('anna@gmail.com', 'wrong')
        and: 'an account exists with that email'
        def account = account('anna@gmail.com', 'Secret123')
        accountRepo.save(account)
        when:
        target.login(resource)
        then:
        thrown(ForbiddenException)
    }

    def 'login should return a jwt token and the accountId'() {
        given:
        def resource = accountResource('test@gmail.com', 'Secret123')
        and: 'an account exists with that email'
        def account = account('test@gmail.com', encoder.encode('Secret123'))
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
