package backend

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.authority.SimpleGrantedAuthority

class AccountService {
    SpringSecurityService springSecurityService

    @Transactional
    def signup(request) {
        def account = new Account(request)
        account.password = springSecurityService.encodePassword(account.password)

        if (!account.save()) {
            throw new RuntimeException("Failed to create account")
        }

        def userRole = Role.findByAuthority('ROLE_USER')
        if (!userRole) {
            userRole = new Role(authority: 'ROLE_USER').save(flush: true)
        }

        new AccountRole(account: account, role: userRole).save(flush: true)

        return true
    }

    def signin(String username, String password, session) {
        Account account = Account.findByUsername(username)

        if (!account) {
            throw new RuntimeException("Invalid credentials")
        }

        if (!springSecurityService.passwordEncoder.matches(password, account.password)) {
            throw new RuntimeException("Invalid credentials")
        }

        // Set up Spring Security authentication
        def authorities = account.authorities.collect {
            new SimpleGrantedAuthority(it.authority)
        }

        def authToken = new UsernamePasswordAuthenticationToken(
            account.username,
            null,
            authorities
        )
        SecurityContextHolder.context.authentication = authToken

        session.setAttribute('SPRING_SECURITY_CONTEXT', SecurityContextHolder.context)

        return account
    }
}