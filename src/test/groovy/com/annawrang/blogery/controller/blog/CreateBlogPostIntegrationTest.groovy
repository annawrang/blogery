package com.annawrang.blogery.controller.blog

import com.annawrang.blogery.controller.BaseIntegrationTest
import com.annawrang.blogery.controller.BlogController
import com.annawrang.blogery.exception.ForbiddenException
import com.annawrang.blogery.exception.NotFoundException
import com.annawrang.blogery.resource.PostResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration
class CreateBlogPostIntegrationTest extends BaseIntegrationTest {

    @Autowired
    BlogController target

    def 'an exception should be thrown if the blog does not exist'() {
        given:
        setupAuth()
        and:
        def resource = postResource()
        when:
        target.createBlogPost(random(), resource)
        then:
        thrown(NotFoundException)
    }

    def 'an exception should be thrown if the user does not own the blog'() {
        given:
        setupAuth()
        and:
        def blog = blog()
        blogRepo.save(blog)
        and:
        def resource = postResource()
        when:
        target.createBlogPost(blog.blogId, resource)
        then:
        thrown(ForbiddenException)
    }

    def 'the created post resource should be returned'() {
        given:
        def accountId = random()
        setupAuth(accountId)
        and:
        def blog = blog('blog', 'description', random(), accountId)
        blogRepo.save(blog)
        and:
        def resource = postResource()
        when:
        def result = target.createBlogPost(blog.blogId, resource)
        then:
        result.statusCodeValue == 201
        def body = (PostResource) result.body
        body.text == resource.text
        body.title == resource.title
        body.urls == resource.urls
    }
}
