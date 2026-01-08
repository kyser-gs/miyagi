package backend.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableRabbit
class RabbitMQConfig {

    public static final String QUEUE_NAME = "miyagi.queue"
    public static final String EXCHANGE_NAME = "miyagi.exchange"
    public static final String ROUTING_KEY = "miyagi.routing.key"

    @Bean
    Queue queue() {
        return new Queue(QUEUE_NAME, true)
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME)
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY)
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter()
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory)
        template.setMessageConverter(messageConverter)
        return template
    }
}
