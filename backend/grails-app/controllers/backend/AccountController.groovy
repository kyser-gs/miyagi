package backend

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.authority.SimpleGrantedAuthority

class AccountController {
    AccountService accountService
    SpringSecurityService springSecurityService

    def create() {
        def json = request.JSON

        try {
            if (accountService.create(json)) {
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
            // Get authorities from account
            def authorities = account.authorities.collect {
                new SimpleGrantedAuthority(it.authority)
            }

            // Create authentication token and set it in the security context
            def authToken = new UsernamePasswordAuthenticationToken(
                account.username,
                null,
                authorities
            )
            SecurityContextHolder.context.authentication = authToken

            // Save the security context in the session
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
