package com.annawrang.blogery.controller.blog

import com.annawrang.blogery.controller.BaseIntegrationTest
import com.annawrang.blogery.controller.BlogController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.multipart.MultipartFile

import java.nio.file.Files

@SpringBootTest
@ContextConfiguration
class UploadContentIntegrationTest extends BaseIntegrationTest {

    @Autowired
    BlogController target

//    def 'upload content should return the url'() {
//        given:
//        FileInputStream input = new FileInputStream(fileItem);
//        MultipartFile multipartFile = new MockMultipartFile("fileItem",
//                fileItem.getName(), "image/png", IOUtils.toByteArray(input))
//        URL url = Thread.currentThread().getContextClassLoader().getResource("files/IMG_2408.JPG");
////        File file = new File(url.getPath())
//        byte[] content = null
//        try {
//            content = Files.readAllBytes(url);
//        } catch (final IOException e) {
//        }
//        def multiPartFile = new MockMultipartFile('IMG_2408',
//                'IMG_2408', 'file', content)
//        when:
//        def result = target.postMedia(multiPartFile)
//        then:
//        result != null
//        println result
//
//    }
}
