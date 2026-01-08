package backend

import backend.config.RabbitMQConfig
import com.rabbitmq.client.Channel
import grails.gorm.transactions.Transactional
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.amqp.support.AmqpHeaders

class ConsumerService {
    ElasticService elasticService

    @Transactional
    @RabbitListener(queues = '#{T(backend.config.RabbitMQConfig).QUEUE_NAME}')
    def receiveMessage(@Payload String userId, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) {
        println "Received message to index user: ${userId}"

        try {
            def user = User.get(userId as Long)
            if (user) {
                elasticService.indexUser(user)
                println "Successfully indexed user ${userId} in Elasticsearch"
            } else {
                println "User ${userId} not found for indexing"
            }

            channel.basicAck(deliveryTag, false)
            println "Message acknowledged for user ${userId}"
        } catch (Exception e) {
            println "Failed to process message: ${e.message}"

            channel.basicNack(deliveryTag, false, true)
            println "Message rejected and requeued for user ${userId}"
        }
    }
}
