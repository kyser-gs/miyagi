package backend

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.security.core.context.SecurityContextHolder

class AccountController {
    AccountService accountService
    SpringSecurityService springSecurityService

    def signup() {
        def json = request.JSON

        try {
            if (accountService.signup(json)) {
                render([success: true, username: json.username] as JSON)
            }
        } catch (Exception e) {
            render([success: false, message: e.message] as JSON)
        }
    }

    def signin() {
        def json = request.JSON

        try {
            Account account = accountService.signin(json.username, json.password, session)
            render([success: true, username: account.username] as JSON)
        } catch (Exception e) {
            render([success: false, message: e.message] as JSON)
        }
    }

    def signout() {
        SecurityContextHolder.clearContext()
        session.invalidate()
        render([success: true, message: 'Signed out successfully'] as JSON)
    }
}
