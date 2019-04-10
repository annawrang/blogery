package com.annawrang.blogery

import com.annawrang.blogery.model.Account
import com.annawrang.blogery.resource.AccountResource
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.testcontainers.shaded.org.apache.http.auth.UsernamePasswordCredentials
import spock.lang.Specification

class BaseAccountTest extends Specification {

    /** Inject authentication **/
    def setupAuth(UUID userId = UUID.randomUUID(), String password = 'Secret1234', accountId = random()) {
        SecurityContext securityContext = Mock(SecurityContext)
        Authentication authentication = Mock(Authentication)
        authentication.getName() >> userId
        authentication.getPrincipal() >> new UsernamePasswordCredentials(
                userId.toString(),
                password
        )
        securityContext.getAuthentication() >> authentication
        SecurityContextHolder.setContext(securityContext)
        userId
    }

    /** Remove authentication **/
    def setupNoAuth() {
        SecurityContext securityContext = Mock(SecurityContext)
        SecurityContextHolder.setContext(securityContext)
    }

    def accountResource(email = 'anna@gmail.com', password = 'Secret123') {
        new AccountResource(
                email: email,
                password: password,
                accountId: UUID.randomUUID()
        )
    }

    def account(email = 'anna@gmail.com', password = 'Secret1234') {
        new Account(
                email: email,
                password: password,
                accountId: UUID.randomUUID()
        )
    }

    def random() {
        UUID.randomUUID()
    }
}
