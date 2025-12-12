package backend

import grails.converters.JSON
import grails.gorm.transactions.Transactional

class UserController {
    UserService userService;

    def save() {
        println "===== SAVE METHOD CALLED ====="
        println "Request JSON: ${request.JSON}"
        if (userService.saveUser(request.JSON)) {
            render([success: true] as JSON)
        } else {
            response.status = 400
            render([success: false] as JSON)
        }
    }

    def list() {
        render(userService.getUsers() as JSON)
    }

    def show(Long id) {
        def user = User.get(id)
        if (user) {
            render(user as JSON)
        } else {
            response.status = 404
            render([message: 'User not found'] as JSON)
        }
    }
}
