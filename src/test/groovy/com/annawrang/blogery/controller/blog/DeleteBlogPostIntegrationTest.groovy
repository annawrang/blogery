package com.annawrang.blogery.controller.blog

import com.annawrang.blogery.controller.BaseIntegrationTest
import com.annawrang.blogery.controller.BlogController
import com.annawrang.blogery.exception.ForbiddenException
import com.annawrang.blogery.exception.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration
class DeleteBlogPostIntegrationTest extends BaseIntegrationTest {

    @Autowired
    BlogController target

    def 'an exception should be thrown if the blog does not exist'() {
        given:
        setupAuth()
        when:
        target.deletePost(random(), random())
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
        target.deletePost(blog.blogId, random())
        then:
        thrown(ForbiddenException)
    }

    def 'the post should be deleted'() {
        given:
        def accountId = random()
        setupAuth(accountId)
        and:
        def blog = blog('blog', 'description', random(), accountId)
        blogRepo.save(blog)
        and:
        def postId = random()
        def post = post(blog.blogId, postId)
        postRepo.save(post)
        when:
        def result = target.deletePost(blog.blogId, postId)
        then:
        result.statusCodeValue == 204
        !postRepo.findByBlogIdAndPostId(blog.blogId, postId).isPresent()
    }
}
