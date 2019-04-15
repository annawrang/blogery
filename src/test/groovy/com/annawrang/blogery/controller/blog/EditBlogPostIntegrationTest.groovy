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
class EditBlogPostIntegrationTest extends BaseIntegrationTest {

    @Autowired
    BlogController target

    def 'an exception should be thrown if the blog does not exist'() {
        given:
        setupAuth()
        and:
        def resource = postResource()
        when:
        target.editPost(random(), random(), resource)
        then:
        thrown(NotFoundException)
    }

    def 'an exception should be thrown if the authenticated user is not the blog owner'() {
        given:
        def blog = blog('blog', 'description', random(), random())
        blogRepo.save(blog)
        and:
        def postId = random()
        def post = post(blog.blogId, postId)
        postRepo.save(post)
        and:
        def resource = postResource()
        and:
        setupAuth()
        when:
        target.editPost(blog.blogId, postId, resource)
        then:
        thrown(ForbiddenException)
    }

    def 'an exception should be thrown if the post does not exist'() {
        given:
        def userId = random()
        def blog = blog('blog', 'description', random(), userId)
        blogRepo.save(blog)
        and:
        def resource = postResource(random(), 'new title', 'new text', ['new url'])
        and:
        setupAuth(userId)
        when:
        target.editPost(blog.blogId, random(), resource)
        then:
        thrown(NotFoundException)
    }

    def 'the post should be returned'() {
        given:
        def userId = random()
        def blog = blog('blog', 'description', random(), userId)
        blogRepo.save(blog)
        and:
        def postId = random()
        def post = post(blog.blogId, postId)
        postRepo.save(post)
        and:
        def resource = postResource(random(), 'new title', 'new text', ['new url'])
        and:
        setupAuth(userId)
        when:
        def result = target.editPost(blog.blogId, postId, resource)
        then:
        result.statusCodeValue == 200
        def body = (PostResource) result.body
        body.postId == postId
        body.urls == resource.urls
        body.title == resource.title
        body.text == resource.text
    }
}
