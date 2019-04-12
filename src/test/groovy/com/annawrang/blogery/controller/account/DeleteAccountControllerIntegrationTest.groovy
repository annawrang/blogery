package com.annawrang.blogery.controller.account

import com.annawrang.blogery.controller.AccountController
import com.annawrang.blogery.controller.BaseIntegrationTest
import com.annawrang.blogery.exception.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration
class DeleteAccountControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    AccountController target

    def 'createAccount should throw exception if the account does not exist'() {
        given:
        setupAuth()
        when:
        target.deleteAccount(random())
        then:
        thrown(NotFoundException)
    }

    def 'createAccount should delete the account'() {
        given:
        def account = account('anna@gmail.com', 'Secret123')
        accountRepo.save(account)
        and:
        setupAuth(account.accountId, 'Secret123')
        when:
        target.deleteAccount(account.accountId)
        then:
        !accountRepo.findByAccountId(account.accountId).isPresent()
    }
}
