package backend

import java.text.SimpleDateFormat
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured

class UserController {
    UserService userService
    ProducerService producerService

    @Secured(['ROLE_USER'])
    def save() {
        def user = userService.saveUser(request.JSON)

        if (user) {
            // Send message to RabbitMQ for async Elasticsearch indexing
            try {
                producerService.sendMessage(user.id.toString())
                println "Published message to index user ${user.id}"
            } catch (Exception e) {
                println "Failed to publish user indexing message: ${e.message}"
            }

            render([success: true] as JSON)
        } else {
            response.status = 400
            render([success: false] as JSON)
        }
    }

    def list() {
        def sdf = new SimpleDateFormat("yyyy-MM-dd")

        def startDate = params.startDate ? sdf.parse(params.startDate) : null
        def endDate = params.endDate ? sdf.parse(params.endDate) : null
        def name = params.name ?: null
        def page = params.page ? params.int('page') : 0
        def size = params.size ? params.int('size') : 10

        render(userService.getUsers(name, startDate, endDate, page, size) as JSON)
    }
}
