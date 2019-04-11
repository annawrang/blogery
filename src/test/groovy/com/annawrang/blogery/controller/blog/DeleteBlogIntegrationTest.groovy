package com.annawrang.blogery.controller.blog

import com.annawrang.blogery.controller.BlogController
import com.annawrang.blogery.exception.ForbiddenException
import com.annawrang.blogery.exception.NotFoundException
import com.annawrang.blogery.exception.UnauthorizedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration
class DeleteBlogIntegrationTest extends BaseBlogIntegrationTest {

    @Autowired
    BlogController target

    def 'an exception should be thrown if the blog does not exist'() {
        given:
        setupAuth()
        when:
        target.deleteBlog(random())
        then:
        thrown(NotFoundException)
    }

    def 'an exception should be thrown if the user does not own the blog'() {
        given:
        setupAuth()
        and:
        def blog = blog()
        blogRepo.save(blog)
        when:
        target.deleteBlog(blog.blogId)
        then:
        thrown(ForbiddenException)
    }

    def 'the blog should be deleted'() {
        given:
        def blog = blog()
        blogRepo.save(blog)
        and: 'auth is setup'
        setupAuth(blog.accountId)
        when:
        def result = target.deleteBlog(blog.blogId)
        then:
        result.statusCodeValue == 204
    }
}
