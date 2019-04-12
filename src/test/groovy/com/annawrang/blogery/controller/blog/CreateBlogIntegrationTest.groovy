package com.annawrang.blogery.controller.blog

import com.annawrang.blogery.controller.BaseIntegrationTest
import com.annawrang.blogery.controller.BlogController
import com.annawrang.blogery.exception.BadRequestException
import com.annawrang.blogery.resource.BlogResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration
class CreateBlogIntegrationTest extends BaseIntegrationTest {

    @Autowired
    BlogController target

    def 'an exception should be thrown if the blog name is taken'() {
        given:
        def resource = blogResource('blog')
        and: 'auth is setup'
        setupAuth()
        and: 'the blog name is taken'
        def blog = blog('blog')
        blogRepo.save(blog)
        when:
        target.createBlog(resource)
        then:
        thrown(BadRequestException)
    }

    def 'the created blog resource should be returned'() {
        given:
        def resource = blogResource('blog')
        and: 'auth is setup'
        setupAuth()
        when:
        def result = target.createBlog(resource)
        then:
        result.statusCodeValue == 201
        def body = (BlogResource) result.body
        body.blogId != null
        body.name == resource.name
    }
}
