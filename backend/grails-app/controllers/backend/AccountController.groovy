package backend

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.authority.SimpleGrantedAuthority

class AccountController {
    AccountService accountService
    SpringSecurityService springSecurityService

    def signup() {
        def json = request.JSON

        try {
            if (accountService.signup(json)) {
                render([success: true, username: json.username] as JSON)
            } else {
                response.status = 400
                render([success: false, message: 'Failed to create account'] as JSON)
            }
        } catch (Exception e) {
            response.status = 400
            render([success: false, message: e.message] as JSON)
        }
    }

    def signin() {
        def json = request.JSON

        Account account = accountService.signin(json.username, json.password)

        if (account) {
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

            render([success: true, username: account.username] as JSON)
        } else {
            response.status = 401
            render([success: false, message: 'Invalid credentials'] as JSON)
        }
    }

    def signout() {
        SecurityContextHolder.clearContext()
        session.invalidate()
        render([success: true, message: 'Signed out successfully'] as JSON)
    }
}
