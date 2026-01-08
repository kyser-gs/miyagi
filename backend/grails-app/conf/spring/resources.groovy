import backend.config.RabbitMQConfig
import backend.ConsumerService

// Place your Spring DSL code here
beans = {
    rabbitMQConfig(RabbitMQConfig)
    consumerService(ConsumerService) {
        elasticService = ref('elasticService')
    }
}
