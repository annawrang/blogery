package com.annawrang.blogery.controller.blog

import com.annawrang.blogery.controller.BaseIntegrationTest
import com.annawrang.blogery.controller.BlogController
import com.annawrang.blogery.resource.PostResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.hateoas.PagedResources
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration
class GetBlogPostsIntegrationTest extends BaseIntegrationTest {

    @Autowired
    BlogController target

    def 'an empty list should be returned if there are no posts'() {
        given:
        setupNoAuth()
        and:
        def pageable = new PageRequest(1, 2)
        when:
        def result = target.getAllPosts(random(), pageable)
        then:
        result.statusCodeValue == 200
        def body = (PagedResources<PostResource>) result.body
        body.metadata.number == pageable.pageNumber
        body.metadata.size == pageable.pageSize
        body.metadata.totalElements == 0
        body.metadata.totalPages == 0
        body.content.size() == 0
    }

    def 'a list of posts should be returned for the blog'() {
        given:
        setupNoAuth()
        and:
        def blog = blog('blog', 'description', random(), random())
        blogRepo.save(blog)
        and:
        def pageable = new PageRequest(0, 2)
        and:
        def post1 = post(blog.blogId, random())
        def post2 = post(blog.blogId, random())
        def post3 = post(blog.blogId, random())
        postRepo.save(post1.addComment(comment()))
        postRepo.save(post2.addComment(comment()))
        postRepo.save(post3)
        when:
        def result = target.getAllPosts(blog.blogId, pageable)
        then:
        result.statusCodeValue == 200
        def body = (PagedResources<PostResource>) result.body
        body.metadata.number == pageable.pageNumber
        body.metadata.size == pageable.pageSize
        body.metadata.totalElements == 3
        body.metadata.totalPages == 2
        body.content.size() == 2
        body.content.comments.size() == 2
    }
}
