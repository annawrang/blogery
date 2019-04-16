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
class DeleteCommentIntegrationTest extends BaseIntegrationTest {

    @Autowired
    BlogController target

    def 'an exception should be thrown if the blog does not exist'() {
        given:
        setupAuth()
        when:
        target.deleteComment(random(), random(), random())
        then:
        thrown(NotFoundException)
    }

    def 'an exception should be thrown if the user is not the blog owner'() {
        given:
        setupAuth()
        and:
        def blog = blog('blog', 'description', random())
        blogRepo.save(blog)
        when:
        target.deleteComment(blog.blogId, random(), random())
        then:
        thrown(ForbiddenException)
    }

    def 'an exception should be thrown if the post does not exist'() {
        given:
        def accountId = random()
        setupAuth(accountId)
        and:
        def blog = blog('blog', 'description', random(), accountId)
        blogRepo.save(blog)
        when:
        target.deleteComment(blog.blogId, random(), random())
        then:
        thrown(NotFoundException)
    }

    def 'an exception should be thrown if the comment does not exist'() {
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
        target.deleteComment(blog.blogId, postId, random())
        then:
        thrown(NotFoundException)
    }

    def 'the comment should be deleted'() {
        given:
        def accountId = random()
        setupAuth(accountId)
        and:
        def blog = blog('blog', 'description', random(), accountId)
        blogRepo.save(blog)
        and:
        def postId = random()
        def post = post(blog.blogId, postId)
        def comment = comment()
        post.addComment(comment)
        postRepo.save(post)
        postRepo.findByBlogIdAndPostId(blog.blogId, postId).get().comments.size() == 1
        when:
        def result = target.deleteComment(blog.blogId, postId, comment.commentId)
        then:
        result.statusCodeValue == 204
        postRepo.findByBlogIdAndPostId(blog.blogId, postId).get().comments.size() == 0
    }
}
