package top.woaibocai.user.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import top.woaibocai.model.Enum.RabbitMqEnum;

/**
 * @program: woaibocai-parent
 * @description: 结合枚举封装RabbitMq
 * @author: LikeBocai
 * @create: 2024/7/20 10:22
 **/
@Component
@Slf4j
public class RabbitMqUtils {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void convertAndSend(Object object) {
        rabbitTemplate.convertAndSend(object);
    }
    public void convertAndSend(String routingKey,Object object) {
        rabbitTemplate.convertAndSend(routingKey,object);
    }
    public void convertAndSend(String routingKey, Object object, CorrelationData correlationData) {
        rabbitTemplate.convertAndSend(routingKey,object,correlationData);
    }
    public void convertAndSend(String routingKey, Object message, MessagePostProcessor messagePostProcessor) {
        rabbitTemplate.convertAndSend(routingKey,message,messagePostProcessor);
    }
    public void convertAndSend(RabbitMqEnum rabbitMqEnum, Object message) {
        rabbitTemplate.convertAndSend(rabbitMqEnum.getExchange(), rabbitMqEnum.getRoutingKey(), message);;
    }
    public void convertAndSend(RabbitMqEnum rabbitMqEnum, Object message, CorrelationData correlationData) {
        rabbitTemplate.convertAndSend(rabbitMqEnum.getExchange(), rabbitMqEnum.getRoutingKey(), message,correlationData);;
    }
    public void convertAndSend(Object message, MessagePostProcessor messagePostProcessor){
        rabbitTemplate.convertAndSend(message,messagePostProcessor);
    }
    public void convertAndSend(Object message, MessagePostProcessor messagePostProcessor, CorrelationData correlationData) {
        rabbitTemplate.convertAndSend(message,messagePostProcessor,correlationData);
    }
    public void convertAndSend(String routingKey,Object message, MessagePostProcessor messagePostProcessor, CorrelationData correlationData) {
        rabbitTemplate.convertAndSend(routingKey,message,messagePostProcessor,correlationData);
    }
    public void convertAndSend(RabbitMqEnum rabbitMqEnum, Object message, MessagePostProcessor messagePostProcessor) {
        rabbitTemplate.convertAndSend(rabbitMqEnum.getExchange(), rabbitMqEnum.getRoutingKey(), message,messagePostProcessor);
    }
    public void convertAndSend(RabbitMqEnum rabbitMqEnum, Object message, MessagePostProcessor messagePostProcessor,CorrelationData correlationData) {
        rabbitTemplate.convertAndSend(rabbitMqEnum.getExchange(), rabbitMqEnum.getRoutingKey(), message,messagePostProcessor,correlationData);
    }
}
