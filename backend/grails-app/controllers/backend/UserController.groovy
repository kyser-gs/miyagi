package backend

import grails.converters.JSON
import grails.gorm.transactions.Transactional

class UserController {

    @Transactional
    def save() {
        def user = new User(request.JSON)

        if (user.save()) {
            render([user: user] as JSON)
        } else {
            response.status = 400
            render([errors: user.errors.allErrors] as JSON)
        }
    }

    @Transactional
    def delete(Long id) {
        def user = User.get(id)
        if (user) {
            user.delete()
            render([message: 'User deleted'] as JSON)
        } else {
            response.status = 404
            render([message: 'User not found'] as JSON)
        }
    }

    def list() {
        def users = User.list()
        render(users as JSON)
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
