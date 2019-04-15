package com.annawrang.blogery.service.blog

import com.annawrang.blogery.exception.NotFoundException
import com.annawrang.blogery.repository.BlogRepository
import com.annawrang.blogery.repository.PostRepository
import com.annawrang.blogery.service.AccountService
import com.annawrang.blogery.service.BaseUnitTest
import com.annawrang.blogery.service.BlogService

import javax.validation.Validation

class GetBlogPostTest extends BaseUnitTest {
    BlogService target

    BlogRepository blogRepository

    PostRepository postRepository

    AccountService accountService

    def setup() {
        accountService = Mock(AccountService)
        blogRepository = Mock(BlogRepository)
        postRepository = Mock(PostRepository)
        target = new BlogService(
                accountService: accountService,
                blogRepository: blogRepository,
                postRepository: postRepository,
                validator: Validation.buildDefaultValidatorFactory().getValidator()
        )
    }

    def 'an exception should be thrown if the post does not exist'() {
        given:
        setupNoAuth()
        when:
        target.getPost(random(), random())
        then:
        1 * postRepository.findByBlogIdAndPostId(_ as UUID, _ as UUID) >> Optional.empty()
        0 * _
        thrown(NotFoundException)
    }
}
