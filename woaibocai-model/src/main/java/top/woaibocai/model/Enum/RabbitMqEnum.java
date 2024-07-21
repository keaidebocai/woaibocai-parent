package top.woaibocai.model.Enum;

import lombok.Getter;

/**
 * @program: woaibocai-parent
 * @description: rabbitMq的交换机和队列
 * @author: LikeBocai
 * @create 2024/7/20 10:07
 **/
@Getter
public enum RabbitMqEnum {

    // 死信队列(这个主要计算时间，到期的送到投递队列，没过期的放回等待过期的队列)
    COMPUTE_LETTER("exchange.email.compute.letter","routing.key.email.compute.letter"),
    // 等待过期的队列(笔记里的正常队列)
    AWAIT_LETTER("exchange.email.await.letter","routing.key.email.await.letter"),
    // 投递队列
    DELIVER_LETTER("exchange.email.deliver.letter","routing.key.email.deliver.letter")
    ;

    // 死信队列(这个主要计算时间，到期的送到投递队列，没过期的放回等待过期的队列)
    public static final String QUEUE_EMAIL_COMPUTE_LETTER = "queue.email.compute.letter";
    // 等待过期的队列(笔记里的正常队列)
    public static final String QUEUE_EMAIL_AWAIT_LETTER = "queue.email.await.letter";
    // 投递队列
    public static final String QUEUE_EMAIL_DELIVER_LETTER = "queue.email.deliver.letter";

    private String exchange;
    private String routingKey;

    private RabbitMqEnum(String exchange,String routingKey) {
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

}
