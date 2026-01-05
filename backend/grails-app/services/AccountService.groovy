package backend

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class AccountService {
    SpringSecurityService springSecurityService
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder()

    @Transactional
    def create(request) {
        def account = new Account(request)
        account.password = springSecurityService.encodePassword(account.password)

        if (!account.save()) {
            return false
        }

        // Find or create ROLE_USER
        def userRole = Role.findByAuthority('ROLE_USER')
        if (!userRole) {
            userRole = new Role(authority: 'ROLE_USER').save(flush: true)
        }

        // Assign the role to the account
        new AccountRole(account: account, role: userRole).save(flush: true)

        return true
    }

    def signin(String username, String password){
        Account account = Account.findByUsername(username)

        if (!account){
            return false
        }

        if (springSecurityService.passwordEncoder.matches(password, account.password)) {
            return account
        }

        return null
    }
}