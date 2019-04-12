package com.annawrang.blogery.controller.account

import com.annawrang.blogery.controller.AccountController
import com.annawrang.blogery.controller.BaseIntegrationTest
import com.annawrang.blogery.resource.BlogResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration
class GetAccountBlogsControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    AccountController target

    def 'an empty list should be returned if account has no blogs'() {
        given:
        def accountId = random()
        setupAuth(accountId)
        when:
        def result = target.getBlogsForAccount(accountId)
        then:
        result.statusCodeValue == 200
        def blogs = (List<BlogResource>) result.body
        blogs.size() == 0
    }

    def 'a list should be returned with blogs for account'() {
        given:
        def accountId = random()
        setupAuth(accountId)
        and:
        def blog1 = blog('name1', 'description', random(), accountId)
        def blog2 = blog('name2', 'description', random(), accountId)
        blogRepo.saveAll([blog1, blog2])
        when:
        def result = target.getBlogsForAccount(accountId)
        then:
        result.statusCodeValue == 200
        println result.body
        def blogs = (List<BlogResource>) result.body
        blogs.size() == 2
    }
}
