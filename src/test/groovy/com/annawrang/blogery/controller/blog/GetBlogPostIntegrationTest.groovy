package com.annawrang.blogery.controller.blog

import com.annawrang.blogery.controller.BaseIntegrationTest
import com.annawrang.blogery.controller.BlogController
import com.annawrang.blogery.exception.NotFoundException
import com.annawrang.blogery.resource.PostResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration
class GetBlogPostIntegrationTest extends BaseIntegrationTest {

    @Autowired
    BlogController target

    def 'an exception should be thrown if the blog does not exist'() {
        given:
        setupAuth()
        when:
        target.getPost(random(), random())
        then:
        thrown(NotFoundException)
    }

    def 'the post should be returned'() {
        given:
        def blog = blog('blog', 'description', random(), random())
        blogRepo.save(blog)
        and:
        def postId = random()
        def post = post(blog.blogId, postId)
        postRepo.save(post)
        when:
        def result = target.getPost(blog.blogId, postId)
        then:
        result.statusCodeValue == 200
        def body = (PostResource) result.body
        body.postId == postId
        body.urls == post.urls
        body.title == post.title
        body.text == post.text
    }
}
