package backend

import backend.config.RabbitMQConfig
import org.springframework.amqp.rabbit.core.RabbitTemplate

class ProducerService {
    RabbitTemplate rabbitTemplate

    def sendMessage(String message) {
        try {
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                message
            )
            println "Message sent: ${message}"
            return [success: true, message: "Message sent successfully"]
        } catch (Exception e) {
            println "Failed to send message: ${e.message}"
            return [success: false, error: e.message]
        }
    }
}
