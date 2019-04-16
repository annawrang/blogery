package com.annawrang.blogery.controller.blog

import com.annawrang.blogery.controller.BaseIntegrationTest
import com.annawrang.blogery.controller.BlogController
import com.annawrang.blogery.exception.NotFoundException
import com.annawrang.blogery.resource.CommentResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration
class CreateCommentIntegrationTest extends BaseIntegrationTest{

    @Autowired
    BlogController target

    def 'an exception should be thrown if the post does not exist'() {
        given:
        def resource = commentResource()
        when:
        target.postComment(random(), random(), resource)
        then:
        thrown(NotFoundException)
    }

    def 'the created comment should be returned'() {
        given:
        def accountId = random()
        and:
        def blog = blog('blog', 'description', random(), accountId)
        blogRepo.save(blog)
        and:
        def postId = random()
        def post = post(blog.blogId, postId)
        postRepo.save(post)
        and:
        def resource = commentResource()
        postRepo.findByBlogIdAndPostId(blog.blogId, postId).get().comments.size() == 0
        when:
        def result = target.postComment(blog.blogId, postId, resource)
        then:
        result.statusCodeValue == 200
        def body = (CommentResource) result.body
        body.text == resource.text
        body.commentId != null
        body.createdBy == resource.createdBy
        postRepo.findByBlogIdAndPostId(blog.blogId, postId).get().comments.size() == 1
    }
}
