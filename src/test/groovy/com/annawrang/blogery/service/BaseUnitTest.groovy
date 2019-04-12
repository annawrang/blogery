package com.annawrang.blogery.service

import com.annawrang.blogery.model.Account
import com.annawrang.blogery.model.Blog
import com.annawrang.blogery.resource.AccountResource
import com.annawrang.blogery.resource.BlogResource
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.testcontainers.shaded.org.apache.http.auth.UsernamePasswordCredentials
import spock.lang.Specification

import java.time.Instant

class BaseUnitTest extends Specification {

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

    def blogResource(name = 'blog name', description = 'description', blogId = random()) {
        new BlogResource(
                blogId: blogId,
                name: name,
                description: description,
                createdAt: Instant.now(),
                profilePictureUrl: 'url',
                backgroundPictureUrl: 'url'
        )
    }

    def blog(name = 'blog name', description = 'description', blogId = random(), accountId = random()) {
        new Blog(
                blogId: blogId,
                name: name,
                description: description,
                createdAt: Instant.now(),
                profilePictureUrl: 'url',
                backgroundPictureUrl: 'url',
                accountId: accountId
        )
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
