package com.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Data
@Slf4j
public class RabbitHelper {

    private String queueName;
    private String message;
    private RabbitTemplate rabbitTemplate = new RabbitTemplate();

    public RabbitHelper(RabbitTemplate rabbitTemplate, String queue, String meg ) {
        this.queueName = queue;
        this.message = meg;
        this.rabbitTemplate = rabbitTemplate;
    }
    public RabbitHelper() {

    }

    public void startThread(RabbitTemplate rabbitTemplate, String queueName, String message) {

        new Thread(()->{
            log.info(queueName);
            rabbitTemplate.convertAndSend(queueName, message);
        }).start();
    }


}
