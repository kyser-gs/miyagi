package backend

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService

class AccountService {
    SpringSecurityService springSecurityService

    @Transactional
    def signup(request) {
        def account = new Account(request)
        account.password = springSecurityService.encodePassword(account.password)

        if (!account.save()) {
            return false
        }

        def userRole = Role.findByAuthority('ROLE_USER')
        if (!userRole) {
            userRole = new Role(authority: 'ROLE_USER').save(flush: true)
        }

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